package com.siyamand.aws.dynamodb.core.sdk.lambda

interface FunctionBuilder {
    fun build(
            name: String,
            code: String,
            role: String,
            layers: List<String>,
            environmentVariables: Map<String, String>,
            subnetIds: List<String>,
            securityGroupIds: List<String>
            ): CreateFunctionRequestEntity
    fun buildLayer(name: String, description: String, resourcePath: String): CreateLayerEntity
    fun buildEventSourceCreateRequest(sourceArn: String, functionName: String): CreateEventSourceRequestEntity
}