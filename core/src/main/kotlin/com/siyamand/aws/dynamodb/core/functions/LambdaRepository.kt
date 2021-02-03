package com.siyamand.aws.dynamodb.core.functions

import com.siyamand.aws.dynamodb.core.monitoring.entities.*
import com.siyamand.aws.dynamodb.core.common.AWSBaseRepository
import com.siyamand.aws.dynamodb.core.resource.ResourceEntity

interface LambdaRepository : AWSBaseRepository {
    suspend fun getList(): List<FunctionEntity>
    suspend fun getDetail(name: String): FunctionDetailEntity
    suspend fun add(request: CreateFunctionRequestEntity): ResourceEntity
    suspend fun add(request: CreateEventSourceRequestEntity): ResourceEntity
}