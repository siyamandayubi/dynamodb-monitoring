package com.siyamand.aws.dynamodb.core.database

import com.siyamand.aws.dynamodb.core.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.secretManager.SecretManagerRepository
import com.siyamand.aws.dynamodb.core.workflow.WorkflowContext
import com.siyamand.aws.dynamodb.core.workflow.WorkflowResult
import com.siyamand.aws.dynamodb.core.workflow.WorkflowStep

class CreateDatabaseWorkflowStep(
        private var credentialProvider: CredentialProvider,
        private val databaseRepository: DatabaseRepository,
        private val secretManagerRepository: SecretManagerRepository) : WorkflowStep() {
    override val name: String = "CreateDatabase"

    override suspend fun execute(context: WorkflowContext, params: Map<String, String>): WorkflowResult {
        TODO("Not yet implemented")
    }

    override suspend fun isWaiting(context: WorkflowContext, params: Map<String, String>): WorkflowResult {
        TODO("Not yet implemented")
    }

    override suspend fun initialize() {
        this.credentialProvider = credentialProvider.threadSafe()
    }
}