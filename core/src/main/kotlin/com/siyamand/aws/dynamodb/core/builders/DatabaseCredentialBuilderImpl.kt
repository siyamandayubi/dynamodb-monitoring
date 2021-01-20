package com.siyamand.aws.dynamodb.core.builders

import com.siyamand.aws.dynamodb.core.entities.database.DatabaseCredentialEntity
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