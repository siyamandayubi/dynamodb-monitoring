package com.siyamand.aws.dynamodb.core.table

interface TableService {
    suspend fun getTables(): List<TableEntity>
    suspend fun getTableDetail(tableName: String): TableDetailEntity?
}