package com.siyamand.aws.dynamodb.core.entities

import java.time.Instant

class FunctionEntity(
        var name: String,
        val runtime: String? = null,
        val role: String? = null,
        val description: String? = null,
        val packageType: String? = null,
        val handler: String? = null,
        val timeout: Int? = null,
        val memorySize: Int? = null
)

class FunctionDetailEntity(val entity: FunctionEntity, val code: FunctionCodeLocationEntity)

class FunctionCodeEntity(
        val zipFile: String? = null,
        val s3Bucket: String? = null,
        val s3Key: String? = null,
        val s3ObjectVersion: String? = null,
        val imageUri: String? = null
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
        val code: String? = null,
        val description: String? = null,
        val timeout: Int? = null,
        val memorySize: Int?,
        val publish: Boolean? = null,
        val packageType: String? = null,
        val tags: Map<String, String>? = null,
        val codeSigningConfigArn: String? = null)

class CreateEventSourceRequestEntity(val eventSourceArn: String? = null){

     val functionName: String? = null

     val enabled: Boolean? = null

     val batchSize: Int? = null

     val maximumBatchingWindowInSeconds: Int? = null

     val parallelizationFactor: Int? = null

     val startingPosition: String? = null

     val startingPositionTimestamp: Instant? = null

     val maximumRecordAgeInSeconds: Int? = null

     val bisectBatchOnFunctionError: Boolean? = null

     val maximumRetryAttempts: Int? = null
}