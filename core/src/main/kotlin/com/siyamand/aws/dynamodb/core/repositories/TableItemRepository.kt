package com.siyamand.aws.dynamodb.core.repositories

import com.siyamand.aws.dynamodb.core.entities.TableItemEntity

interface TableItemRepository : AWSBaseRepository {
    suspend fun add(tableItem: TableItemEntity)
}