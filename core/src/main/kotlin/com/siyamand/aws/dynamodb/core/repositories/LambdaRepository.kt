package com.siyamand.aws.dynamodb.core.repositories

import com.siyamand.aws.dynamodb.core.entities.FunctionEntity

interface LambdaRepository : AWSCRUDRepository<FunctionEntity, LambdaRepository> {
}