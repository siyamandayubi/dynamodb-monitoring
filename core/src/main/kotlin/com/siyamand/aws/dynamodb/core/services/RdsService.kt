package com.siyamand.aws.dynamodb.core.services

import com.siyamand.aws.dynamodb.core.entities.ResourceEntity

interface RdsService {
    suspend fun createDbInstance(name: String): ResourceEntity
}