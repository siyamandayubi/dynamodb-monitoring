package com.siyamand.aws.dynamodb.core.sdk.s3

interface S3Service {
    suspend fun createBucket(): String
    suspend fun getBuckets(): List<String>
    suspend fun addObject(name: String, monitoringId:String, data: ByteArray): S3ObjectEntity
    suspend fun enableBucketVersions(name: String)
}