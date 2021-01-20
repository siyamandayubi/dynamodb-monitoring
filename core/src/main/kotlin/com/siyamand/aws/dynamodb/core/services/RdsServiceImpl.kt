package com.siyamand.aws.dynamodb.core.services

import com.siyamand.aws.dynamodb.core.builders.DatabaseCredentialBuilder
import com.siyamand.aws.dynamodb.core.builders.RdsBuilder
import com.siyamand.aws.dynamodb.core.builders.SecretBuilder
import com.siyamand.aws.dynamodb.core.entities.CreateSecretEntity
import com.siyamand.aws.dynamodb.core.entities.ResourceEntity
import com.siyamand.aws.dynamodb.core.repositories.RdsRepository
import com.siyamand.aws.dynamodb.core.repositories.ResourceRepository
import com.siyamand.aws.dynamodb.core.repositories.SecretManagerRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RdsServiceImpl(
        private val credentialProvider: CredentialProvider,
        private val rdsBuilder: RdsBuilder,
        private val secretBuilder: SecretBuilder,
        private val databaseCredentialBuilder: DatabaseCredentialBuilder,
        private val rdsRepository: RdsRepository,
        private val resourceRepository: ResourceRepository,
        private val secretManagerRepository: SecretManagerRepository) : RdsService {
    override suspend fun createDbInstance(name: String): ResourceEntity {
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
        val createRequest = rdsBuilder.build(name, databaseCredential, credentialResource)

        return rdsRepository.createDatabase(createRequest)
    }

    private suspend fun initialize() {
        val credential = credentialProvider.getCredential()
                ?: throw SecurityException("No Credential has been provided");

        rdsRepository.initialize(credential, credentialProvider.getRegion());
        secretManagerRepository.initialize(credential, credentialProvider.getRegion())
        resourceRepository.initialize(credential, credentialProvider.getRegion())
    }
}