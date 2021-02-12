package com.siyamand.aws.dynamodb.core.database

import com.siyamand.aws.dynamodb.core.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.rds.RdsRepository
import com.siyamand.aws.dynamodb.core.resource.ResourceRepository
import com.siyamand.aws.dynamodb.core.secretManager.SecretManagerRepository
import com.siyamand.aws.dynamodb.core.workflow.*
import kotlinx.serialization.json.Json

class CreateDatabaseWorkflowStep(
        private var credentialProvider: CredentialProvider,
        private val databaseRepository: DatabaseRepository,
        private val resourceRepository: ResourceRepository,
        private val rdsRepository: RdsRepository,
        private val secretManagerRepository: SecretManagerRepository) : WorkflowStep() {
    override val name: String = "CreateDatabase"

    override suspend fun execute(context: WorkflowContext, params: Map<String, String>): WorkflowResult {

        // check SECRET_ARN_KEY
        if (!context.sharedData.containsKey(Keys.SECRET_ARN_KEY)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "No SecretKey ARN '${Keys.SECRET_ARN_KEY}' exists in shared data")
        }

        // check RDS key
        if (!context.sharedData.containsKey(Keys.RDS_ARN_KEY)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "No RDS ARN '${Keys.RDS_ARN_KEY}' exists in shared data")
        }

        // check database name
        if (!context.sharedData.containsKey(Keys.DATABASE_NAME)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "The '${Keys.DATABASE_NAME}' field is mandatory")
        }

        val rdsResource = resourceRepository.convert(context.sharedData[Keys.RDS_ARN_KEY]!!)
        val rdsList = rdsRepository.getRds(rdsResource.service)
        if (!rdsList.any()) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "No Rds has been found with the name '${rdsResource.service}'")
        }
        val rds = rdsList.first()

        val secretResource = resourceRepository.convert(context.sharedData[Keys.SECRET_ARN_KEY]!!)
        var existingSecret = secretManagerRepository.getSecret(secretResource.service)
        val credential = Json.decodeFromString(DatabaseCredentialEntity.serializer(), existingSecret!!.secretData)


        val databaseConnectionEntity = DatabaseConnectionEntity(credential, rds.endPoint, context.sharedData[Keys.DATABASE_NAME]!!, rds.port)
        databaseRepository.createDatabase(databaseConnectionEntity)

        return WorkflowResult(WorkflowResultType.SUCCESS, mapOf(), "")
    }

    override suspend fun isWaiting(context: WorkflowContext, params: Map<String, String>): WorkflowResult {
        return execute(context, params)
    }

    override suspend fun initialize() {
        this.credentialProvider = credentialProvider.threadSafe()
    }
}