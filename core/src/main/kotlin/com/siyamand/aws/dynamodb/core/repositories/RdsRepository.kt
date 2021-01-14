package com.siyamand.aws.dynamodb.core.repositories

import com.siyamand.aws.dynamodb.core.entities.RdsEntity
import com.siyamand.aws.dynamodb.core.entities.ResourceEntity
import com.siyamand.aws.dynamodb.core.entities.database.CreateProxyEntity

interface RdsRepository : AWSBaseRepository {
    suspend fun getRds(name: String): RdsEntity
    suspend fun addProxy(entity: CreateProxyEntity): ResourceEntity
}