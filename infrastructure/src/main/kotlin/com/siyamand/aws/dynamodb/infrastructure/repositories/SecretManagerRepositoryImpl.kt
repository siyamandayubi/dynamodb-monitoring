package com.siyamand.aws.dynamodb.infrastructure.repositories

import com.siyamand.aws.dynamodb.core.entities.CreateSecretEntity
import com.siyamand.aws.dynamodb.core.entities.ResourceEntity
import com.siyamand.aws.dynamodb.core.repositories.SecretManagerRepository
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilder
import com.siyamand.aws.dynamodb.infrastructure.mappers.ResourceMapper
import com.siyamand.aws.dynamodb.infrastructure.mappers.SecretManagerMapper

class SecretManagerRepositoryImpl(private val clientBuilder: ClientBuilder) : SecretManagerRepository, AwsBaseRepositoryImpl() {
    override fun addSecret(entity: CreateSecretEntity): ResourceEntity {
        val client = getClient(clientBuilder::buildAsyncSecretsManagerClient)
        val request = SecretManagerMapper.convert(entity)
        val response = client.createSecret(request)
        return ResourceMapper.convert(response.arn())
    }
}