package com.siyamand.aws.dynamodb.infrastructure.repositories

import com.siyamand.aws.dynamodb.core.database.DatabaseConnectionEntity
import com.siyamand.aws.dynamodb.core.database.DatabaseCredentialEntity
import com.siyamand.aws.dynamodb.core.database.DatabaseRepository
import java.sql.DriverManager


class DatabaseRepositoryImpl : DatabaseRepository {
    override fun createDatabase(databaseConnectionEntity: DatabaseConnectionEntity) {
        val url = getJdbcUrl(databaseConnectionEntity, false)

        // SQL command to create a database in MySQL.
        val sql = "CREATE DATABASE IF NOT EXISTS ${databaseConnectionEntity.databaseName}"
        executeSql(url, databaseConnectionEntity.databaseCredentialEntity, sql)
    }

    override fun executeSql(databaseConnectionEntity: DatabaseConnectionEntity, sql: String) {
        val url = getJdbcUrl(databaseConnectionEntity, true)
        executeSql(url, databaseConnectionEntity.databaseCredentialEntity, sql)
    }

    private fun executeSql(url: String, databaseCredentialEntity: DatabaseCredentialEntity, sql: String) {
        try {
            DriverManager.getConnection(
                    url,
                    databaseCredentialEntity.userName,
                    databaseCredentialEntity.password)
                    .use { conn ->
                        conn.prepareStatement(sql)
                                .use { stmt -> stmt.execute() }
                    }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
    private fun getJdbcUrl(databaseConnectionEntity: DatabaseConnectionEntity, includeDatabaseName : Boolean): String {
        var url = "jdbc:mysql://${databaseConnectionEntity.endPoint}:${databaseConnectionEntity.port}"

        if (includeDatabaseName){
            url = url + "/${databaseConnectionEntity.databaseName}"
        }

        return url
    }
}