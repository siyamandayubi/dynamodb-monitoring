package com.siyamand.aws.dynamodb.core.repositories

import com.siyamand.aws.dynamodb.core.entities.*
import com.siyamand.aws.dynamodb.core.entities.s3.CreateBucketRequestEntity
import com.siyamand.aws.dynamodb.core.entities.s3.CreateS3ObjectRequestEntity
import com.siyamand.aws.dynamodb.core.entities.s3.S3ObjectEntity

interface S3Repository : AWSBaseRepository {
    suspend fun addBucket(entity: CreateBucketRequestEntity): String
    suspend fun addObject(entity: CreateS3ObjectRequestEntity): S3ObjectEntity
    suspend fun getBuckets(): List<String>
}