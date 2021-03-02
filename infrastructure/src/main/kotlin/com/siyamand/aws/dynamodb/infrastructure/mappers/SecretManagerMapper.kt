package com.siyamand.aws.dynamodb.infrastructure.mappers

import com.siyamand.aws.dynamodb.core.sdk.resource.TagEntity
import com.siyamand.aws.dynamodb.core.sdk.secretManager.CreateSecretEntity
import com.siyamand.aws.dynamodb.core.sdk.secretManager.SecretDetailEntity
import com.siyamand.aws.dynamodb.core.sdk.secretManager.SecretEntity
import software.amazon.awssdk.services.secretsmanager.model.Tag
import software.amazon.awssdk.services.secretsmanager.model.CreateSecretRequest
import software.amazon.awssdk.services.secretsmanager.model.DescribeSecretResponse
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse

class SecretManagerMapper {
    companion object {
        fun convert(entity: CreateSecretEntity): CreateSecretRequest {
            val builder = CreateSecretRequest
                    .builder()
                    .name(entity.name)
                    .description(entity.description)
                    .secretString(entity.secretData)

            if (entity.tags.any()) {
                builder.tags(entity.tags.map { (Tag.builder().key(it.name).value(it.value).build()) })
            }

            return builder.build()
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
                    response.description() ?: "",
                    response.tags().map { TagEntity(it.key(), it.value()) })
        }
    }
}