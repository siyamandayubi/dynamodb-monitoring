package com.siyamand.aws.dynamodb.core.sdk.s3

import com.siyamand.aws.dynamodb.core.common.AWSBaseRepository
import com.siyamand.aws.dynamodb.core.common.PageResultEntity

interface S3Repository : AWSBaseRepository {
    suspend fun addBucket(entity: CreateBucketRequestEntity): String
    suspend fun addObject(entity: CreateS3ObjectRequestEntity): S3ObjectEntity
    suspend fun getBuckets(): List<String>
    suspend fun enableBucketVersioning(bucket: String): String
    suspend fun getObjectVersions(bucket: String, prefix: String, marker: String): PageResultEntity<S3ObjectVersionEntity>
    suspend fun getObject(bucket: String, prefix: String): S3ObjectEntityWithData
}