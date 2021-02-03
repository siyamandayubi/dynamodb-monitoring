package com.siyamand.aws.dynamodb.core.dynamodb

interface TableService {
    suspend fun getTables(): List<TableEntity>
    suspend fun getTableDetail(tableName: String): TableDetailEntity?
}