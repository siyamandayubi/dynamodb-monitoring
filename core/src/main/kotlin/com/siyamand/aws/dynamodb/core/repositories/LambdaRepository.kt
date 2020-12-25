package com.siyamand.aws.dynamodb.core.repositories

import com.siyamand.aws.dynamodb.core.entities.FunctionEntity

interface LambdaRepository : AWSBaseRepository {
    suspend fun getList(): List<FunctionEntity>
}