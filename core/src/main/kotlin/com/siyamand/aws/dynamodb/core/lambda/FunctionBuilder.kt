package com.siyamand.aws.dynamodb.core.lambda

interface FunctionBuilder {
    fun build(name: String, code: String, role :String): CreateFunctionRequestEntity
    fun buildLayer(name: String, description: String, resourcePath: String): CreateLayerEntity
}