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

    override suspend fun execute(instance: WorkflowInstance, context: WorkflowContext, params: Map<String, String>): WorkflowResult {
        if (!context.sharedData.containsKey(Keys.PROXY_NAME)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "No Proxy key ${Keys.PROXY_NAME} found")
        }

        if (!context.sharedData.containsKey(Keys.RDS_ARN_KEY)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "No RDS key ${Keys.RDS_ARN_KEY} found")
        }

        // check RDS key
        if (!params.containsKey(Keys.PROXY_TARGET_GROUP_NAME)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "No '${Keys.PROXY_TARGET_GROUP_NAME}' exists in params")
        }

        credentialProvider.initializeRepositories(resourceRepository, rdsRepository)

        val proxyName = context.sharedData[Keys.PROXY_NAME]!!
        val rdsArn = context.sharedData[Keys.RDS_ARN_KEY]!!

        val rdsList = rdsRepository.getRds(rdsArn)
        if (!rdsList.any()) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "No Rds has been found with the name '${rdsArn}'")
        }
        val rds = rdsList.first()

        val targetGroup = rdsRepository.getDbProxyTargetGroups(proxyName).items.first { it.isDefault }
        val result = rdsRepository.registerDbProxyTarget(
                CreateDbProxyTargetEntity(
                        proxyName,
                        targetGroup.groupName ?: "",
                        listOf(rds.instanceName))).first()

        context.sharedData[Keys.PROXY_TARGET_GROUP_ARN] = result.targetResource.arn
        return WorkflowResult(WorkflowResultType.SUCCESS, mapOf(Keys.PROXY_TARGET_GROUP_ARN to result.targetResource.arn), "")
    }

    override suspend fun isWaiting(instance: WorkflowInstance, context: WorkflowContext, params: Map<String, String>): WorkflowResult {
        return execute(instance, context, params)
    }

    override suspend fun initialize() {
        this.credentialProvider = credentialProvider.threadSafe()
    }
}