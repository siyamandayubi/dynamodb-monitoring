package com.siyamand.aws.dynamodb.core.database

interface DatabaseRepository {
    fun createDatabase(databaseConnectionEntity: DatabaseConnectionEntity)
}