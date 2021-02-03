package com.siyamand.aws.dynamodb.core.database

import java.util.*

class DatabaseCredentialBuilderImpl : DatabaseCredentialBuilder {
    private companion object {
        const val USER_NAME = "root"
    }

    override fun build(): DatabaseCredentialEntity {
        val password = UUID.randomUUID().toString()
        return DatabaseCredentialEntity(USER_NAME, password)
    }
}