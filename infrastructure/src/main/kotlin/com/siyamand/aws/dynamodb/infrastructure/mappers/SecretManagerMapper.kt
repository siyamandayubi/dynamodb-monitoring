package com.siyamand.aws.dynamodb.infrastructure.mappers

import com.siyamand.aws.dynamodb.core.entities.CreateSecretEntity
import software.amazon.awssdk.services.secretsmanager.model.CreateSecretRequest

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
    }
}