package com.siyamand.aws.dynamodb.core.builders

import com.siyamand.aws.dynamodb.core.entities.database.DatabaseCredentialEntity

interface DatabaseCredentialBuilder {
    fun build(): DatabaseCredentialEntity
}