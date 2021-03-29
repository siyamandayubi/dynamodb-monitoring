package com.siyamand.aws.dynamodb.core.sdk.rds

import com.siyamand.aws.dynamodb.core.sdk.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.common.initializeRepositories
import com.siyamand.aws.dynamodb.core.monitoring.MonitoringResourcePersister
import com.siyamand.aws.dynamodb.core.sdk.rds.entities.CreateDbProxyTargetEntity
import com.siyamand.aws.dynamodb.core.sdk.resource.ResourceRepository
import com.siyamand.aws.dynamodb.core.workflow.*

class CreateRdsProxyTargetGroupWorkflowStep(private var credentialProvider: CredentialProvider,
                                            private val rdsRepository: RdsRepository,
                                            private val resourceRepository: ResourceRepository,
                                            private val monitoringResourcePersister: MonitoringResourcePersister) : WorkflowStep() {
    override val name: String
        get() = "CreateRdsProxyTargetGroup"

    override suspend fun execute(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {
        val context = instance.context
        if (!context.sharedData.containsKey(Keys.PROXY_NAME)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "No Proxy key ${Keys.PROXY_NAME} found")
        }

        if (!context.sharedData.containsKey(Keys.RDS_ARN_KEY)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "No RDS key ${Keys.RDS_ARN_KEY} found")
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
        val existingTarget = rdsRepository.getDbProxyTargets(targetGroup.groupName, proxyName)
        if (existingTarget.items.any()) {
            val arn = existingTarget.items.first().targetResource?.arn ?: ""
            context.sharedData[Keys.PROXY_TARGET_GROUP_ARN] = arn
            return WorkflowResult(WorkflowResultType.SUCCESS, mapOf(Keys.PROXY_TARGET_GROUP_ARN to arn), "")
        }

        val result = rdsRepository.registerDbProxyTarget(
                CreateDbProxyTargetEntity(
                        proxyName,
                        targetGroup.groupName,
                        listOf(rds.instanceName))).first()

        val arn = result.targetResource?.arn ?: ""
        if (!arn.isNullOrEmpty()) {
            monitoringResourcePersister.persist(instance.id, arn)
        }
        context.sharedData[Keys.PROXY_TARGET_GROUP_ARN] = arn
        return WorkflowResult(WorkflowResultType.SUCCESS, mapOf(Keys.PROXY_TARGET_GROUP_ARN to arn), "")
    }

    override suspend fun isWaiting(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {
        return execute(instance, owner, params)
    }

    override suspend fun initialize() {
        monitoringResourcePersister.threadSafe()
        this.credentialProvider = credentialProvider.threadSafe()
    }
}