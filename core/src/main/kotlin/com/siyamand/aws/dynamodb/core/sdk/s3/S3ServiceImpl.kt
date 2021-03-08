package com.siyamand.aws.dynamodb.core.sdk.s3

import com.siyamand.aws.dynamodb.core.sdk.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.helpers.ZipHelper
import com.siyamand.aws.dynamodb.core.common.MonitorConfigProvider

class S3ServiceImpl(
        private var credentialProvider: CredentialProvider,
        private val s3Repository: S3Repository,
        private val monitorConfigProvider: MonitorConfigProvider) : S3Service {
    override suspend fun createBucket(): String {
        initialize()
        return s3Repository.addBucket(CreateBucketRequestEntity(monitorConfigProvider.getS3BucketDefaultName()))
    }

    override suspend fun enableBucketVersions(name: String) {
        initialize()
        s3Repository.enableBucketVersioning(name)
    }

    override suspend fun getObject(name: String): S3ObjectEntityWithData {
        initialize()
        return s3Repository.getObject(monitorConfigProvider.getS3BucketDefaultName(), name)
    }

    override suspend fun addObject(name: String, mimeType: String, monitoringId: String, data: ByteArray): S3ObjectEntity {
        initialize()
        val buckets = s3Repository.getBuckets();
        var bucket = buckets.filter { it == monitorConfigProvider.getS3BucketDefaultName() }.firstOrNull()
        if (bucket == null) {
            bucket = s3Repository.addBucket(CreateBucketRequestEntity(monitorConfigProvider.getS3BucketDefaultName()))
        }
        return s3Repository.addObject(CreateS3ObjectRequestEntity(
                bucket,
                name,
                data,
                mimeType,
                mapOf(
                        monitorConfigProvider.getMonitoringVersionTagName() to monitorConfigProvider.getMonitoringVersionValue(),
                        monitorConfigProvider.getMonitoringMetadataIdTagName() to monitoringId)))
    }

    override suspend fun getObjectVersions(prefix: String): List<S3ObjectVersionEntity> {
        initialize()
        val returnValue: MutableList<S3ObjectVersionEntity> = mutableListOf()
        val bucket = monitorConfigProvider.getS3BucketDefaultName()
        var marker = ""
        var lastResult = s3Repository.getObjectVersions(bucket, prefix, marker)
        returnValue.addAll(lastResult.items)
        while (!lastResult.nextPageToken.isNullOrEmpty()) {
            marker = lastResult.nextPageToken ?: ""
            lastResult = s3Repository.getObjectVersions(bucket, prefix, marker)
            returnValue.addAll(lastResult.items)
        }

        return returnValue
    }

    override suspend fun threadSafe() {
        credentialProvider = credentialProvider.threadSafe()
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