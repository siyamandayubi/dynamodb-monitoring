package com.siyamand.aws.dynamodb.core.database

import com.siyamand.aws.dynamodb.core.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.common.initializeRepositories
import com.siyamand.aws.dynamodb.core.rds.RdsRepository
import com.siyamand.aws.dynamodb.core.resource.ResourceRepository
import com.siyamand.aws.dynamodb.core.secretManager.SecretManagerRepository
import com.siyamand.aws.dynamodb.core.workflow.*
import kotlinx.serialization.json.Json

class CreateDatabaseWorkflowStep(
        credentialProvider: CredentialProvider,
        databaseRepository: DatabaseRepository,
        resourceRepository: ResourceRepository,
        rdsRepository: RdsRepository,
        secretManagerRepository: SecretManagerRepository) : DatabaseWorkflowStep(credentialProvider, databaseRepository, resourceRepository, rdsRepository, secretManagerRepository) {
    override val name: String = "CreateDatabase"

    override suspend fun execute(instance: WorkflowInstance, context: WorkflowContext, params: Map<String, String>): WorkflowResult {

        return execute(context, params) { databaseConnectionEntity ->
            databaseRepository.createDatabase(databaseConnectionEntity)

            WorkflowResult(WorkflowResultType.SUCCESS, mapOf(), "")
        }
    }

    override suspend fun isWaiting(instance: WorkflowInstance, context: WorkflowContext, params: Map<String, String>): WorkflowResult {
        return execute(instance, context, params)
    }

    override suspend fun initialize() {
        this.credentialProvider = credentialProvider.threadSafe()
    }
}