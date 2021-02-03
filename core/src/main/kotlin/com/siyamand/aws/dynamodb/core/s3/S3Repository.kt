package com.siyamand.aws.dynamodb.core.s3

import com.siyamand.aws.dynamodb.core.common.AWSBaseRepository

interface S3Repository : AWSBaseRepository {
    suspend fun addBucket(entity: CreateBucketRequestEntity): String
    suspend fun addObject(entity: CreateS3ObjectRequestEntity): S3ObjectEntity
    suspend fun getBuckets(): List<String>
}