package com.siyamand.aws.dynamodb.core.sdk.lambda

import com.siyamand.aws.dynamodb.core.common.AWSBaseRepository
import com.siyamand.aws.dynamodb.core.common.PageResultEntity
import com.siyamand.aws.dynamodb.core.sdk.resource.ResourceEntity

interface LambdaRepository : AWSBaseRepository {
    suspend fun getList(): List<FunctionEntity>
    suspend fun getDetail(name: String): FunctionDetailEntity?
    suspend fun add(request: CreateFunctionRequestEntity): ResourceEntity
    suspend fun add(request: CreateEventSourceRequestEntity): ResourceEntity
    suspend fun add(entity: CreateLayerEntity): FunctionLayerEntity
    suspend fun getLayer(name: String): PageResultEntity<FunctionLayerEntity>
}