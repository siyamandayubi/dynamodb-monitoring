package com.siyamand.aws.dynamodb.core.dynamodb

import com.siyamand.aws.dynamodb.core.monitoring.entities.item.AttributeValueEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.item.TableItemEntity
import com.siyamand.aws.dynamodb.core.common.AWSBaseRepository
import kotlinx.coroutines.flow.Flow

interface TableItemRepository : AWSBaseRepository {
    suspend fun add(tableItem: TableItemEntity): TableItemEntity
    fun getList(tableName:String, startKey: Map<String, AttributeValueEntity>?): Flow<TableItemEntity>
}