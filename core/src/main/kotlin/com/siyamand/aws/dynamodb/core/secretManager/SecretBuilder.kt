package com.siyamand.aws.dynamodb.core.secretManager

import com.siyamand.aws.dynamodb.core.database.DatabaseCredentialEntity

interface SecretBuilder {
    fun buildCreateRequest(name: String, obj: DatabaseCredentialEntity): CreateSecretEntity
}