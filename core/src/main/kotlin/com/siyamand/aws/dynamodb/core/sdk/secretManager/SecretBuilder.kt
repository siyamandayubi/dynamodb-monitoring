package com.siyamand.aws.dynamodb.core.sdk.secretManager

import com.siyamand.aws.dynamodb.core.database.DatabaseCredentialEntity

interface SecretBuilder {
    fun buildCreateRequest(name: String, obj: DatabaseCredentialEntity, metadataId: String): CreateSecretEntity
}