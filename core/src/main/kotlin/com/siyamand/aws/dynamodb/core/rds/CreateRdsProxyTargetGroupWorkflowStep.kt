package com.siyamand.aws.dynamodb.core.rds

import com.siyamand.aws.dynamodb.core.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.common.initializeRepositories
import com.siyamand.aws.dynamodb.core.network.VpcRepository
import com.siyamand.aws.dynamodb.core.resource.ResourceRepository
import com.siyamand.aws.dynamodb.core.role.RoleService
import com.siyamand.aws.dynamodb.core.workflow.*

class CreateRdsProxyTargetGroupWorkflowStep(private var credentialProvider: CredentialProvider,
                                            private val rdsRepository: RdsRepository,
                                            private val resourceRepository: ResourceRepository) : WorkflowStep() {
    override val name: String
        get() = "CreateRdsProxyTargetGroup"

    override suspend fun execute(context: WorkflowContext, params: Map<String, String>): WorkflowResult {
        if (!context.sharedData.containsKey(Keys.PROXY_ARN_KEY)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "No Proxy key ${Keys.PROXY_ARN_KEY} found")
        }

        if (!context.sharedData.containsKey(Keys.RDS_ARN_KEY)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "No RDS key ${Keys.RDS_ARN_KEY} found")
        }

        // check RDS key
        if (!params.containsKey(Keys.PROXY_TARGET_GROUP_NAME)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "No '${Keys.PROXY_TARGET_GROUP_NAME}' exists in params")
        }

        credentialProvider.initializeRepositories(resourceRepository, rdsRepository)

        val proxyResource = resourceRepository.convert(context.sharedData[Keys.PROXY_ARN_KEY]!!)
        val rdsResource = resourceRepository.convert(context.sharedData[Keys.RDS_ARN_KEY]!!)
        val result = rdsRepository.registerDbProxyTarget(CreateDbProxyTargetEntity(proxyResource.service, params[Keys.PROXY_TARGET_GROUP_NAME]
                ?: "", listOf(rdsResource.service))).first()
        context.sharedData[Keys.PROXY_TARGET_GROUP_ARN] = result.targetResource.arn
        return WorkflowResult(WorkflowResultType.SUCCESS, mapOf(Keys.PROXY_TARGET_GROUP_ARN to result.targetResource.arn), "")
    }

    override suspend fun isWaiting(context: WorkflowContext, params: Map<String, String>): WorkflowResult {
        return execute(context, params)
    }

    override suspend fun initialize() {
        this.credentialProvider = credentialProvider.threadSafe()
    }
}