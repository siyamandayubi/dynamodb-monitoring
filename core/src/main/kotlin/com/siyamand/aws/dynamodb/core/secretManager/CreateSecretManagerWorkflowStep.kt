package com.siyamand.aws.dynamodb.core.secretManager

import com.siyamand.aws.dynamodb.core.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.database.DatabaseCredentialBuilder
import com.siyamand.aws.dynamodb.core.workflow.*
import java.time.LocalDate

class CreateSecretManagerWorkflowStep(
        private var credentialProvider: CredentialProvider,
        private val secretBuilder: SecretBuilder,
        private val databaseCredentialBuilder: DatabaseCredentialBuilder,
        private val secretManagerRepository: SecretManagerRepository) : WorkflowStep() {
    override val name: String = "CreateSecret"

    override suspend fun execute(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {
        initialize()
        val databaseCredential = databaseCredentialBuilder.build()
        val createSecretRequest = secretBuilder.buildCreateRequest(LocalDate.now().toString() + "_", databaseCredential, instance.id)

        var existingSecret = secretManagerRepository.getSecretDetail(createSecretRequest.name)

        var counter = 0;
        while (existingSecret != null) {
            counter++;
            createSecretRequest.name = "${createSecretRequest.name}_$counter";
            existingSecret = secretManagerRepository.getSecretDetail(createSecretRequest.name)
        }

        val credentialResource = secretManagerRepository.addSecret(createSecretRequest)
        instance.context.sharedData[Keys.SECRET_ARN_KEY] = credentialResource.arn
        return WorkflowResult(WorkflowResultType.SUCCESS, mapOf(Keys.SECRET_ARN_KEY to credentialResource.arn), "")
    }

    override suspend fun isWaiting(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {
        return WorkflowResult(WorkflowResultType.SUCCESS, params, "")
    }

    override suspend fun initialize() {
        this.credentialProvider = credentialProvider.threadSafe()
        val credential = credentialProvider.getCredential()
                ?: throw SecurityException("No Credential has been provided");

        secretManagerRepository.initialize(credential, credentialProvider.getRegion())
    }
}