package com.siyamand.aws.dynamodb.core.builders

import com.siyamand.aws.dynamodb.core.entities.CreateFunctionRequestEntity

interface FunctionBuilder {
    fun build(name: String, code: String, role :String): CreateFunctionRequestEntity
}