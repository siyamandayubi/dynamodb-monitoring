package com.siyamand.aws.dynamodb.core.services

import com.siyamand.aws.dynamodb.core.entities.TableDetailEntity
import com.siyamand.aws.dynamodb.core.entities.TableEntity

interface TableService {
    suspend fun getTables(): List<TableEntity>
    suspend fun getTableDetail(tableName: String): TableDetailEntity?
}