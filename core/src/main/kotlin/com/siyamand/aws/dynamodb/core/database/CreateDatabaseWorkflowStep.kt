package com.siyamand.aws.dynamodb.core.database

import com.siyamand.aws.dynamodb.core.sdk.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.sdk.rds.RdsRepository
import com.siyamand.aws.dynamodb.core.sdk.resource.ResourceRepository
import com.siyamand.aws.dynamodb.core.sdk.secretManager.SecretManagerRepository
import com.siyamand.aws.dynamodb.core.workflow.*

class CreateDatabaseWorkflowStep(
        credentialProvider: CredentialProvider,
        databaseRepository: DatabaseRepository,
        resourceRepository: ResourceRepository,
        rdsRepository: RdsRepository,
        secretManagerRepository: SecretManagerRepository) : DatabaseWorkflowStep(credentialProvider, databaseRepository, resourceRepository, rdsRepository, secretManagerRepository) {
    override val name: String = "CreateDatabase"

    override suspend fun execute(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {

        return execute(instance.context, params) { databaseConnectionEntity ->
            databaseRepository.createDatabase(databaseConnectionEntity)

            WorkflowResult(WorkflowResultType.SUCCESS, mapOf(), "")
        }
    }

    override suspend fun isWaiting(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {
        return execute(instance, owner, params)
    }

    override suspend fun initialize() {
        this.credentialProvider = credentialProvider.threadSafe()
    }
}