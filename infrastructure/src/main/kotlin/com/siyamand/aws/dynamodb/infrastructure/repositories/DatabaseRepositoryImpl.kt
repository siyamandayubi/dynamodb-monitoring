package com.siyamand.aws.dynamodb.infrastructure.repositories

import com.siyamand.aws.dynamodb.core.entities.database.DatabaseConnectionEntity
import com.siyamand.aws.dynamodb.core.repositories.DatabaseRepository
import java.sql.DriverManager


class DatabaseRepositoryImpl : DatabaseRepository {
    override fun createDatabase(databaseConnectionEntity: DatabaseConnectionEntity) {
        val url = "jdbc:mysql://${databaseConnectionEntity.endPoint}:${databaseConnectionEntity.port}"

        // SQL command to create a database in MySQL.
        val sql = "CREATE DATABASE IF NOT EXISTS ${databaseConnectionEntity.databaseName}"
        try {
            DriverManager.getConnection(
                    url,
                    databaseConnectionEntity.databaseCredentialEntity.userName,
                    databaseConnectionEntity.databaseCredentialEntity.password)
                    .use { conn ->
                        conn.prepareStatement(sql)
                                .use { stmt -> stmt.execute() }
                    }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}