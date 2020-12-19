package com.siyamand.aws.dynamodb.core.services

import com.siyamand.aws.dynamodb.core.entities.FunctionEntity

interface FunctionService {
    suspend fun getFunctions(): List<FunctionEntity>
}