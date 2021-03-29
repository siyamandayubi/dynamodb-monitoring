package com.siyamand.aws.dynamodb.core.sdk.rds

import com.siyamand.aws.dynamodb.core.sdk.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.common.MonitorConfigProvider
import com.siyamand.aws.dynamodb.core.common.initializeRepositories
import com.siyamand.aws.dynamodb.core.database.DatabaseCredentialEntity
import com.siyamand.aws.dynamodb.core.monitoring.MonitoringResourcePersister
import com.siyamand.aws.dynamodb.core.sdk.resource.ResourceRepository
import com.siyamand.aws.dynamodb.core.sdk.resource.TagEntity
import com.siyamand.aws.dynamodb.core.sdk.secretManager.SecretManagerRepository
import com.siyamand.aws.dynamodb.core.workflow.*
import kotlinx.serialization.json.Json

class CreateRdsInstanceWorkflowStep(
        private val monigoringConfigProvider: MonitorConfigProvider,
        private val rdsRepository: RdsRepository,
        private val secretManagerRepository: SecretManagerRepository,
        private var credentialProvider: CredentialProvider,
        private val resourceRepository: ResourceRepository,
        private val rdsBuilder: RdsBuilder,
        private val monitoringResourcePersister: MonitoringResourcePersister
) : WorkflowStep() {
    override val name: String = "CreateRdsInstance"

    override suspend fun execute(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {
        val sharedData = instance.context.sharedData
        if (!sharedData.containsKey(Keys.SECRET_ARN_KEY)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "No Secret key ${Keys.SECRET_ARN_KEY} found")
        }

        if (!params.containsKey("dbInstanceName")) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "dbInstanceName is mandatory")
        }

        // check database name
        if (!sharedData.containsKey(Keys.DATABASE_NAME)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "The '${Keys.DATABASE_NAME}' field is mandatory")
        }
        val dbInstanceClass = if (sharedData.containsKey(Keys.DB_INSTANCE_CLASS)) sharedData[Keys.DB_INSTANCE_CLASS]!! else ""

        val databaseName = sharedData[Keys.DATABASE_NAME]!!

        val instanceParamName = params["dbInstanceName"]!!
        val instanceName = if (sharedData.containsKey(instanceParamName))
            sharedData[instanceParamName]!!
        else ""

        credentialProvider.initializeRepositories(rdsRepository, secretManagerRepository, resourceRepository)

        val secretEntity = secretManagerRepository.getSecretByArn(sharedData[Keys.SECRET_ARN_KEY]!!)

        // check existing of the instance
        val rdsEntities = rdsRepository.getRds(instanceName)
        if (rdsEntities.any()) {
            val rdsEntity = rdsEntities.first()
            sharedData[Keys.RDS_ARN_KEY] = rdsEntity.resource.arn

            val secretTag: TagEntity = rdsEntity.tags.firstOrNull { it.name == monigoringConfigProvider.getAccessTagName() }
                    ?: return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "The Rds exists, but it doesn't have any tag with the given key: ${monigoringConfigProvider.getAccessTagName()}")

            sharedData[Keys.SECRET_ARN_KEY] = secretTag.value

            // delete new generate secret key
            if (secretEntity?.resourceEntity?.arn != secretTag.value) {
                secretManagerRepository.deleteSecret(secretEntity!!.resourceEntity.arn)
            }

            val workflowResultType = if (rdsEntity.status == "available") WorkflowResultType.SUCCESS else WorkflowResultType.WAITING
            if (workflowResultType == WorkflowResultType.SUCCESS) {
                monitoringResourcePersister.persist(instance.id, rdsEntity.resource.arn)
            }

            return WorkflowResult(workflowResultType, mapOf(Keys.RDS_ARN_KEY to rdsEntity.resource.arn), "")
        }

        if (sharedData.containsKey(Keys.RDS_ARN_KEY)) {
            return isWaiting(instance, instance.context, params)
        }

        val databaseCredential = Json.decodeFromString(DatabaseCredentialEntity.serializer(), secretEntity!!.secretData)

        val createRequest = rdsBuilder.build(instanceName, databaseName, databaseCredential, secretEntity.resourceEntity, instance.id, dbInstanceClass)
        val rdsEntity = rdsRepository.createRds(createRequest)
        monitoringResourcePersister.persist(instance.id, rdsEntity.resource.arn)
        sharedData[Keys.RDS_ARN_KEY] = rdsEntity.resource.arn
        val workflowResultType = if (rdsEntity.status == "available") WorkflowResultType.SUCCESS else WorkflowResultType.WAITING
        return WorkflowResult(workflowResultType, mapOf(Keys.RDS_ARN_KEY to rdsEntity.resource.arn), "")
    }

    override suspend fun isWaiting(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {
        val sharedData = instance.context.sharedData
        if (!sharedData.containsKey(Keys.RDS_ARN_KEY)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "No RDS_ARN_KEY ${Keys.RDS_ARN_KEY} found")
        }

        credentialProvider.initializeRepositories(rdsRepository, secretManagerRepository, resourceRepository)

        val arn = sharedData[Keys.RDS_ARN_KEY]!!
        val rdsEntities = rdsRepository.getRds(arn)
        if (!rdsEntities.any()) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "No RDS ${Keys.RDS_ARN_KEY} found")
        }

        val rdsEntity = rdsEntities.first()

        val workflowResultType = if (rdsEntity.status == "available") WorkflowResultType.SUCCESS else WorkflowResultType.WAITING
        return WorkflowResult(workflowResultType, mapOf(Keys.RDS_ARN_KEY to rdsEntity.resource.arn), "")
    }

    override suspend fun initialize() {
        this.monitoringResourcePersister.threadSafe()
        this.credentialProvider = credentialProvider.threadSafe()
    }

}