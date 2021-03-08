package com.siyamand.aws.dynamodb.core.sdk.s3

interface S3Service {
    suspend fun createBucket(): String
    suspend fun getBuckets(): List<String>
    suspend fun addObject(name: String, mimeType: String, monitoringId: String, data: ByteArray): S3ObjectEntity
    suspend fun enableBucketVersions(name: String)
    suspend fun getObjectVersions(prefix: String): List<S3ObjectVersionEntity>
    suspend fun threadSafe()
    suspend fun getObject(name: String): S3ObjectEntityWithData
}