package com.siyamand.aws.dynamodb.core.sdk.lambda

import com.siyamand.aws.dynamodb.core.common.MonitorConfigProvider
import com.siyamand.aws.dynamodb.core.helpers.ZipHelper
import java.nio.file.Files
import java.nio.file.Paths

class FunctionBuilderImpl(private val monitorConfigProvider: MonitorConfigProvider) : FunctionBuilder {
    companion object {
        const val NODEJS_RUNTIME = "nodejs12.x"
    }

    override fun build(
            name: String,
            code: String,
            role: String,
            layers: List<String>,
            environmentVariables: Map<String, String>,
            subnetIds: List<String>,
            securityGroupIds: List<String>,
    ): CreateFunctionRequestEntity {
        return CreateFunctionRequestEntity(
                name,
                NODEJS_RUNTIME,
                role,
                "index.handler",
                ZipHelper.zip(code, "index.js"),
                "",
                null,
                128,
                true,
                "Zip",
                environmentVariables = environmentVariables,
                layers = layers,
                subnetIds =  subnetIds,
                tags = mapOf(monitorConfigProvider.getMonitoringVersionTagName() to monitorConfigProvider.getMonitoringVersionValue()),
                securityGroupIds = securityGroupIds
        )
    }

    override fun buildLayer(name: String, description: String, resourcePath: String): CreateLayerEntity {
        val uri = javaClass.classLoader.getResource(resourcePath).toURI()
        val bytes = Files.readAllBytes(Paths.get(uri))
        return CreateLayerEntity(name, NODEJS_RUNTIME, bytes, description, "MIT Licence")
    }

    override fun buildEventSourceCreateRequest(sourceArn: String, functionName: String): CreateEventSourceRequestEntity{
        val request= CreateEventSourceRequestEntity(sourceArn)
        request.functionName =functionName
        request.batchSize = 100
        request.enabled = true
        request.maximumBatchingWindowInSeconds = 10
        request.maximumRecordAgeInSeconds = -1
        request.maximumRetryAttempts = 3
        request.startingPosition = "LATEST"
        return request
    }
}
