package com.siyamand.aws.dynamodb.core.repositories

import com.siyamand.aws.dynamodb.core.entities.*

interface LambdaRepository : AWSBaseRepository {
    suspend fun getList(): List<FunctionEntity>
    suspend fun getDetail(name: String): FunctionDetailEntity
    suspend fun add(request: CreateFunctionRequestEntity): ResourceEntity
    suspend fun add(request: CreateEventSourceRequestEntity): ResourceEntity
}