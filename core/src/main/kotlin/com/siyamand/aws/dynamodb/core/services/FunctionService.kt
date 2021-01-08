package com.siyamand.aws.dynamodb.core.services

import com.siyamand.aws.dynamodb.core.entities.FunctionDetailEntity
import com.siyamand.aws.dynamodb.core.entities.FunctionEntity
import com.siyamand.aws.dynamodb.core.entities.ResourceEntity

interface FunctionService {
    suspend fun getFunctions(): List<FunctionEntity>
    suspend fun getDetail(name: String): FunctionDetailEntity
    suspend fun addLambda(functionName: String): ResourceEntity
}