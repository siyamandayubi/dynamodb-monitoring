package com.siyamand.aws.dynamodb.infrastructure.repositories

import com.siyamand.aws.dynamodb.core.sdk.resource.ResourceEntity
import com.siyamand.aws.dynamodb.core.sdk.secretManager.CreateSecretEntity
import com.siyamand.aws.dynamodb.core.sdk.secretManager.SecretDetailEntity
import com.siyamand.aws.dynamodb.core.sdk.secretManager.SecretEntity
import com.siyamand.aws.dynamodb.core.sdk.secretManager.SecretManagerRepository
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilder
import com.siyamand.aws.dynamodb.infrastructure.mappers.ResourceMapper
import com.siyamand.aws.dynamodb.infrastructure.mappers.SecretManagerMapper
import software.amazon.awssdk.services.secretsmanager.model.*

class SecretManagerRepositoryImpl(private val clientBuilder: ClientBuilder) : SecretManagerRepository, AwsBaseRepositoryImpl() {
    override fun addSecret(entity: CreateSecretEntity): ResourceEntity {
        val client = getClient(clientBuilder::buildAsyncSecretsManagerClient)
        val request = SecretManagerMapper.convert(entity)
        val response = client.createSecret(request)
        return ResourceMapper.convert(response.arn())
    }

    override fun getSecretByArn(arn: String): SecretEntity? {
        return getSecretValue(arn)
    }

    override fun getSecretValue(secretId: String): SecretEntity? {
        val client = getClient(clientBuilder::buildAsyncSecretsManagerClient)
        return try {
            val response = client.getSecretValue(GetSecretValueRequest.builder().secretId(secretId).build())
            SecretManagerMapper.convert(response)
        } catch (ex: ResourceNotFoundException) {
            null
        } catch (ex: InvalidRequestException) {
            null
        }
    }

    override fun getSecretDetail(secretId: String): SecretDetailEntity? {
        val client = getClient(clientBuilder::buildAsyncSecretsManagerClient)
        return try {
            val response = client.describeSecret(DescribeSecretRequest
                    .builder()
                    .secretId(secretId)
                    .build())
            SecretManagerMapper.convert(response)
        } catch (ex: ResourceNotFoundException) {
            null
        } catch (ex: InvalidRequestException) {
            null
        }
    }

    override fun deleteSecret(secretId: String) {
        val client = getClient(clientBuilder::buildAsyncSecretsManagerClient)
        client.deleteSecret(DeleteSecretRequest.builder().secretId(secretId).build())
    }
}