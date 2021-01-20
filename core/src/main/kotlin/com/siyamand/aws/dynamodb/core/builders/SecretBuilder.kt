package com.siyamand.aws.dynamodb.core.builders

import com.siyamand.aws.dynamodb.core.entities.CreateSecretEntity
import com.siyamand.aws.dynamodb.core.entities.database.DatabaseCredentialEntity
import java.util.*

interface SecretBuilder {
    fun buildCreateRequest(name: String, obj: DatabaseCredentialEntity):CreateSecretEntity
}