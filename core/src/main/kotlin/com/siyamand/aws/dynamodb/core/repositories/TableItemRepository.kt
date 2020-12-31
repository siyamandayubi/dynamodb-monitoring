package com.siyamand.aws.dynamodb.core.repositories

import com.siyamand.aws.dynamodb.core.entities.item.AttributeValueEntity
import com.siyamand.aws.dynamodb.core.entities.item.TableItemEntity
import kotlinx.coroutines.flow.Flow

interface TableItemRepository : AWSBaseRepository {
    suspend fun add(tableItem: TableItemEntity): TableItemEntity
    fun getList(tableName:String, startKey: Map<String, AttributeValueEntity>?): Flow<TableItemEntity>
}