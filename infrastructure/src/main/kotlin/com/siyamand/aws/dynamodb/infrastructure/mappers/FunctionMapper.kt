package com.siyamand.aws.dynamodb.infrastructure.mappers

import com.siyamand.aws.dynamodb.core.entities.FunctionEntity

class FunctionMapper {
    companion object{

        fun convert(function: software.amazon.awssdk.services.lambda.model.FunctionConfiguration): FunctionEntity{
            return  FunctionEntity(function.functionName())
        }
    }
}