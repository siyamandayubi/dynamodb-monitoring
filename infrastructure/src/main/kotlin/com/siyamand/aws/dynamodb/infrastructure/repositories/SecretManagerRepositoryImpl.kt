package com.siyamand.aws.dynamodb.infrastructure.repositories

import com.siyamand.aws.dynamodb.core.resource.ResourceEntity
import com.siyamand.aws.dynamodb.core.secretManager.CreateSecretEntity
import com.siyamand.aws.dynamodb.core.secretManager.SecretEntity
import com.siyamand.aws.dynamodb.core.secretManager.SecretManagerRepository
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilder
import com.siyamand.aws.dynamodb.infrastructure.mappers.ResourceMapper
import com.siyamand.aws.dynamodb.infrastructure.mappers.SecretManagerMapper
import software.amazon.awssdk.services.secretsmanager.model.DeleteSecretRequest
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest
import software.amazon.awssdk.services.secretsmanager.model.InvalidRequestException
import software.amazon.awssdk.services.secretsmanager.model.ResourceNotFoundException

class SecretManagerRepositoryImpl(private val clientBuilder: ClientBuilder) : SecretManagerRepository, AwsBaseRepositoryImpl() {
    override fun addSecret(entity: CreateSecretEntity): ResourceEntity {
        val client = getClient(clientBuilder::buildAsyncSecretsManagerClient)
        val request = SecretManagerMapper.convert(entity)
        val response = client.createSecret(request)
        return ResourceMapper.convert(response.arn())
    }

    override fun getSecretByArn(arn: String): SecretEntity? {
        val client = getClient(clientBuilder::buildAsyncSecretsManagerClient)
        try {
            val response = client.getSecretValue(GetSecretValueRequest.builder().secretId(arn).build())
            return SecretManagerMapper.convert(response)
        } catch (ex: ResourceNotFoundException) {
            return null
        } catch (ex: InvalidRequestException) {
            return null
        }
    }

    override fun getSecret(secretId: String): SecretEntity? {
        val client = getClient(clientBuilder::buildAsyncSecretsManagerClient)
        try {
            val response = client.getSecretValue(GetSecretValueRequest.builder().secretId(secretId).build())
            return SecretManagerMapper.convert(response)
        } catch (ex: ResourceNotFoundException) {
            return null
        } catch (ex: InvalidRequestException) {
            return null
        }
    }

    override fun deleteSecret(secretId: String) {
        val client = getClient(clientBuilder::buildAsyncSecretsManagerClient)
        client.deleteSecret(DeleteSecretRequest.builder().secretId(secretId).build())
    }
}