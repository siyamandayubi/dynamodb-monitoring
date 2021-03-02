package com.siyamand.aws.dynamodb.core.sdk.dynamodb

interface TableService {
    suspend fun getTables(): List<TableEntity>
    suspend fun getTableDetail(tableName: String): TableDetailEntity?
}