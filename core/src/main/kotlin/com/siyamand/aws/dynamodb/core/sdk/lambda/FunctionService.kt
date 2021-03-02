package com.siyamand.aws.dynamodb.core.sdk.lambda

import com.siyamand.aws.dynamodb.core.sdk.resource.ResourceEntity

interface FunctionService {
    suspend fun getFunctions(): List<FunctionEntity>
    suspend fun getDetail(name: String): FunctionDetailEntity?
    suspend fun addLambda(functionName: String, code: String): ResourceEntity
}