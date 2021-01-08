package com.siyamand.aws.dynamodb.core.repositories

import com.siyamand.aws.dynamodb.core.entities.RdsEntity

interface RdsRepository : AWSBaseRepository {
    suspend fun getRds(name: String): RdsEntity
}