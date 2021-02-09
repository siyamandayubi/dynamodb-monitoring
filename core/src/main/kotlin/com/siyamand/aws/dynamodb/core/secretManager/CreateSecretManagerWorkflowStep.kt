package com.siyamand.aws.dynamodb.core.secretManager

import com.siyamand.aws.dynamodb.core.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.database.DatabaseCredentialBuilder
import com.siyamand.aws.dynamodb.core.workflow.*

class CreateSecretManagerWorkflowStep(
        private val credentialProvider: CredentialProvider,
        private val secretBuilder: SecretBuilder,
        private val databaseCredentialBuilder: DatabaseCredentialBuilder,
        private val secretManagerRepository: SecretManagerRepository) : WorkflowStep() {
    override val name: String
        get() = "CreateSecret"


    override suspend fun execute(context: WorkflowContext, params: Map<String, String>): WorkflowResult {
        initialize()
        val databaseCredential = databaseCredentialBuilder.build()
        val createSecretRequest = secretBuilder.buildCreateRequest(name, databaseCredential)

        var existingSecret = secretManagerRepository.getSecret(createSecretRequest.name)

        var counter = 0;
        while (existingSecret != null) {
            counter++;
            createSecretRequest.name = "${name}_$counter";
            existingSecret = secretManagerRepository.getSecret(createSecretRequest.name)
        }

        val credentialResource = secretManagerRepository.addSecret(createSecretRequest)
        context.sharedData[Keys.SECRET_ARN_KEY] = credentialResource.arn
        return WorkflowResult(WorkflowResultType.SUCCESS, mapOf(Keys.SECRET_ARN_KEY to credentialResource.arn), "")
    }

    override suspend fun isWaiting(context: WorkflowContext, params: Map<String, String>): WorkflowResult {
        return WorkflowResult(WorkflowResultType.SUCCESS, params, "")
    }

    private suspend fun initialize() {
        val credential = credentialProvider.getCredential()
                ?: throw SecurityException("No Credential has been provided");

        secretManagerRepository.initialize(credential, credentialProvider.getRegion())
    }
}