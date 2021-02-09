package com.siyamand.aws.dynamodb.core.dynamodb

import com.siyamand.aws.dynamodb.core.common.AWSBaseRepository
import com.siyamand.aws.dynamodb.core.common.PageResultEntityBase
import kotlinx.coroutines.flow.Flow

interface TableItemRepository : AWSBaseRepository {
    suspend fun add(tableItem: TableItemEntity): TableItemEntity
    suspend fun getList(tableName: String, startKey: Map<String, AttributeValueEntity>?): PageResultEntityBase<TableItemEntity, Map<String, AttributeValueEntity>>
    suspend fun getItem(tableName: String, key: Map<String, AttributeValueEntity>): List<TableItemEntity>
    suspend fun update(entity: TableItemEntity)
}