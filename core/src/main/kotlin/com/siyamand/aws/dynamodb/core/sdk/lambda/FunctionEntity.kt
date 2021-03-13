package com.siyamand.aws.dynamodb.core.sdk.lambda

import com.siyamand.aws.dynamodb.core.sdk.resource.ResourceEntity
import java.time.Instant

class FunctionEntity(
        var name: String,
        val runtime: String? = null,
        val role: String? = null,
        val description: String? = null,
        val packageType: String? = null,
        val handler: String? = null,
        val timeout: Int? = null,
        val memorySize: Int? = null,
        val layers: List<ResourceEntity> = listOf()
)

class FunctionDetailEntity(val entity: FunctionEntity, val code: FunctionCodeLocationEntity)

class FunctionCodeEntity(
        val zipFile: String? = null,
        val s3Bucket: String? = null,
        val s3Key: String? = null,
        val s3ObjectVersion: String? = null,
        val imageUri: String? = null
)

class CreateLayerEntity(
        val layerName: String,
        val runTime: String,
        val code: ByteArray,
        val description: String,
        val licenseInfo: String
)

class FunctionCodeLocationEntity(
        val repositoryType: String? = null,
        val location: String? = null,
        val resolvedImageUri: String? = null,
        val imageUri: String? = null
)

class CreateFunctionRequestEntity(
        val functionName: String? = null,
        val runtime: String? = null,
        val role: String? = null,
        val handler: String? = null,
        val code: ByteArray,
        val description: String? = null,
        val timeout: Int? = null,
        val memorySize: Int?,
        val publish: Boolean? = null,
        val packageType: String? = null,
        val tags: Map<String, String>? = null,
        val layers: List<String> = listOf(),
        val environmentVariables: Map<String, String> = mapOf(),
        val subnetIds: List<String> = listOf(),
        val securityGroupIds: List<String> = listOf(),
        val codeSigningConfigArn: String? = null)

class FunctionLayerEntity(
        val layerVersionEntity: ResourceEntity,
        val description: String,
        val createdDate: String,
        val version: Long,
        val compatibleRuntimes: MutableList<String> = mutableListOf(),
        val licenseInfo: String = ""
)

class FunctionLayerListEntity(
        val name: String,
        val resourceEntity: ResourceEntity
)

class CreateEventSourceRequestEntity(val eventSourceArn: String? = null) {

    var functionName: String? = null

    var enabled: Boolean? = null

    var batchSize: Int? = null

    var maximumBatchingWindowInSeconds: Int? = null

    var parallelizationFactor: Int? = null

    var startingPosition: String? = null

    var startingPositionTimestamp: Instant? = null

    var maximumRecordAgeInSeconds: Int? = null

    var bisectBatchOnFunctionError: Boolean? = null

    var maximumRetryAttempts: Int? = null
}