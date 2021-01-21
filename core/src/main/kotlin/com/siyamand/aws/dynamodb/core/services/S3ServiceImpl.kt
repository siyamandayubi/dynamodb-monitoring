package com.siyamand.aws.dynamodb.core.services

import com.siyamand.aws.dynamodb.core.entities.s3.CreateBucketRequestEntity
import com.siyamand.aws.dynamodb.core.entities.s3.CreateS3ObjectRequestEntity
import com.siyamand.aws.dynamodb.core.entities.s3.S3ObjectEntity
import com.siyamand.aws.dynamodb.core.helpers.ZipHelper
import com.siyamand.aws.dynamodb.core.repositories.S3Repository

class S3ServiceImpl(
        private val credentialProvider: CredentialProvider,
        private val s3Repository: S3Repository,
        private val monitorConfigProvider: MonitorConfigProvider) : S3Service {
    override suspend fun createBucket(): String {
        initialize()
        return s3Repository.addBucket(CreateBucketRequestEntity(monitorConfigProvider.getS3BucketDefaultName()))
    }

    override suspend fun addObject(name: String, code: String): S3ObjectEntity {
        initialize()
        val buckets = s3Repository.getBuckets();
        var bucket = buckets.filter { it == monitorConfigProvider.getS3BucketDefaultName() }.firstOrNull()
        if (bucket == null) {
            bucket = s3Repository.addBucket(CreateBucketRequestEntity(monitorConfigProvider.getS3BucketDefaultName()))
        }
        return s3Repository.addObject(CreateS3ObjectRequestEntity(bucket, name, ZipHelper.zip(code, "index.js")))
    }

    override suspend fun getBuckets(): List<String> {
        initialize()
        return s3Repository.getBuckets()
    }

    private suspend fun initialize() {
        val credential = credentialProvider.getCredential()
                ?: throw SecurityException("No Credential has been provided");

        s3Repository.initialize(credential, credentialProvider.getRegion());
    }
}