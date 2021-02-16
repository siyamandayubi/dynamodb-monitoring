package com.siyamand.aws.dynamodb.core.rds

import com.siyamand.aws.dynamodb.core.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.common.initializeRepositories
import com.siyamand.aws.dynamodb.core.database.DatabaseCredentialEntity
import com.siyamand.aws.dynamodb.core.resource.ResourceRepository
import com.siyamand.aws.dynamodb.core.secretManager.SecretManagerRepository
import com.siyamand.aws.dynamodb.core.workflow.*
import kotlinx.serialization.json.Json
import org.springframework.core.invokeSuspendingFunction

class CreateRdsInstanceWorkflowStep(
        private val rdsRepository: RdsRepository,
        private val secretManagerRepository: SecretManagerRepository,
        private var credentialProvider: CredentialProvider,
        private val resourceRepository: ResourceRepository,
        private val rdsBuilder: RdsBuilder
) : WorkflowStep() {
    override val name: String = "CreateRdsInstance"

    override suspend fun execute(workflowInstance: WorkflowInstance, context: WorkflowContext, params: Map<String, String>): WorkflowResult {
        if (!context.sharedData.containsKey(Keys.SECRET_ARN_KEY)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "No Secret key ${Keys.SECRET_ARN_KEY} found")
        }

        if (!params.containsKey("dbInstanceName")) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "dbInstanceName is mandatory")
        }

        val instanceName = params["dbInstanceName"] ?: ""

        credentialProvider.initializeRepositories(rdsRepository, secretManagerRepository, resourceRepository)

        // check existing of the instance
        val rdsEntities = rdsRepository.getRds(instanceName)
        if (rdsEntities.any()) {
            val rdsEntity = rdsEntities.first()
            context.sharedData[Keys.RDS_ARN_KEY] = rdsEntity.resource.arn

            val workflowResultType = if (rdsEntity.status == "available") WorkflowResultType.SUCCESS else WorkflowResultType.WAITING
            return WorkflowResult(workflowResultType, mapOf(Keys.RDS_ARN_KEY to rdsEntity.resource.arn), "")
        }

        if (context.sharedData.containsKey(Keys.RDS_ARN_KEY)) {
            return isWaiting(workflowInstance, context, params)
        }

        val secretEntity = secretManagerRepository.getSecretByArn(context.sharedData[Keys.SECRET_ARN_KEY]!!)
        val databaseCredential = Json.decodeFromString(DatabaseCredentialEntity.serializer(), secretEntity!!.secretData)

        val createRequest = rdsBuilder.build(instanceName, databaseCredential, secretEntity.resourceEntity, workflowInstance.id)
        val rdsEntity = rdsRepository.createRds(createRequest)
        context.sharedData[Keys.RDS_ARN_KEY] = rdsEntity.resource.arn
        val workflowResultType = if (rdsEntity.status == "available") WorkflowResultType.SUCCESS else WorkflowResultType.WAITING
        return WorkflowResult(workflowResultType, mapOf(Keys.RDS_ARN_KEY to rdsEntity.resource.arn), "")
    }

    override suspend fun isWaiting(workflowInstance: WorkflowInstance, context: WorkflowContext, params: Map<String, String>): WorkflowResult {
        if (!context.sharedData.containsKey(Keys.RDS_ARN_KEY)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "No RDS_ARN_KEY ${Keys.RDS_ARN_KEY} found")
        }

        credentialProvider.initializeRepositories(rdsRepository, secretManagerRepository, resourceRepository)

        val arn = context.sharedData[Keys.RDS_ARN_KEY]!!
        val rdsEntities = rdsRepository.getRds(arn)
        if (!rdsEntities?.any()) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "No RDS ${Keys.RDS_ARN_KEY} found")
        }

        val rdsEntity = rdsEntities.first()

        val workflowResultType = if (rdsEntity.status == "available") WorkflowResultType.SUCCESS else WorkflowResultType.WAITING
        return WorkflowResult(workflowResultType, mapOf(Keys.RDS_ARN_KEY to rdsEntity.resource.arn), "")
    }

    override suspend fun initialize() {
        this.credentialProvider = credentialProvider.threadSafe()
    }

}