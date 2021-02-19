package com.siyamand.aws.dynamodb.infrastructure.mappers

import com.siyamand.aws.dynamodb.core.resource.TagEntity
import com.siyamand.aws.dynamodb.core.secretManager.CreateSecretEntity
import com.siyamand.aws.dynamodb.core.secretManager.SecretDetailEntity
import com.siyamand.aws.dynamodb.core.secretManager.SecretEntity
import software.amazon.awssdk.services.secretsmanager.model.CreateSecretRequest
import software.amazon.awssdk.services.secretsmanager.model.DescribeSecretResponse
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse

class SecretManagerMapper {
    companion object{
        fun convert(entity: CreateSecretEntity): CreateSecretRequest {
            return CreateSecretRequest
                    .builder()
                    .name(entity.name)
                    .description(entity.description)
                    .secretString(entity.secretData)
                    .build()

        }

        fun convert(response: GetSecretValueResponse): SecretEntity {
            return SecretEntity(
                    response.name(),
                    response.secretString(),
                    ResourceMapper.convert(response.arn()), response.createdDate())
        }

        fun convert(response: DescribeSecretResponse): SecretDetailEntity {
            return SecretDetailEntity(
                    response.name(),
                    ResourceMapper.convert(response.arn()),
                    response.createdDate(),
                    response.lastAccessedDate(),
                    response.deletedDate(),
                    response.description() ?:"",
                    response.tags().map { TagEntity(it.key(), it.value()) })
        }
    }
}