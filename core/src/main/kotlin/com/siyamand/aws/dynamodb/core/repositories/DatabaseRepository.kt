package com.siyamand.aws.dynamodb.core.repositories

import com.siyamand.aws.dynamodb.core.entities.database.DatabaseConnectionEntity

interface DatabaseRepository {
    fun createDatabase(databaseConnectionEntity: DatabaseConnectionEntity)
}