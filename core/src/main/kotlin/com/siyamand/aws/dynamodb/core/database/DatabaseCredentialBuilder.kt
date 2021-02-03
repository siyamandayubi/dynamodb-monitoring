package com.siyamand.aws.dynamodb.core.database

interface DatabaseCredentialBuilder {
    fun build(): DatabaseCredentialEntity
}