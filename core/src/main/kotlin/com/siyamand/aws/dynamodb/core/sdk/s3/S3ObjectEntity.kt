package com.siyamand.aws.dynamodb.core.sdk.s3

open class S3ObjectEntity(val key: String, val bucket: String, val eTag: String) {
}

class S3ObjectEntityWithData(
        key: String,
        bucket: String,
        eTag: String,
        val versionId : String,
        val data: ByteArray) : S3ObjectEntity(
        key,
        bucket,
        eTag)

