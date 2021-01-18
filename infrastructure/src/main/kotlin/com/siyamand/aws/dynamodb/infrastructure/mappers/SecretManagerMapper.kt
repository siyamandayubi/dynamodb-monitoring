package com.siyamand.aws.dynamodb.infrastructure.mappers

import com.siyamand.aws.dynamodb.core.entities.CreateSecretEntity
import com.siyamand.aws.dynamodb.core.entities.SecretEntity
import software.amazon.awssdk.services.secretsmanager.model.CreateSecretRequest
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

        fun convert(response: GetSecretValueResponse):SecretEntity{
            return SecretEntity(response.name(), response.secretString(),ResourceMapper.convert(response.arn()), response.createdDate())
        }
    }
}