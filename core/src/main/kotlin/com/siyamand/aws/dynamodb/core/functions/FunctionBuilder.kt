package com.siyamand.aws.dynamodb.core.functions

interface FunctionBuilder {
    fun build(name: String, code: String, role :String): CreateFunctionRequestEntity
}