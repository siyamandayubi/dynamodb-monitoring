package com.siyamand.aws.dynamodb.core.entities.database

class DatabaseConnectionEntity(
        val databaseCredentialEntity: DatabaseCredentialEntity,
        val endPoint: String,
        val databaseName: String,
        val port: Int) {
}