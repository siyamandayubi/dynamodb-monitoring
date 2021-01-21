package com.siyamand.aws.dynamodb.core.services

import com.siyamand.aws.dynamodb.core.entities.s3.S3ObjectEntity

interface S3Service {
    suspend fun createBucket(): String
    suspend fun getBuckets(): List<String>
    suspend fun addObject(name: String, code: String): S3ObjectEntity
}