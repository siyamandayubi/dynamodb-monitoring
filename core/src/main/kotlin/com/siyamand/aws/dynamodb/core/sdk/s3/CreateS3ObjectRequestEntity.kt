package com.siyamand.aws.dynamodb.core.sdk.s3

class CreateS3ObjectRequestEntity(
        val bucket: String,
        val key: String,
        val data: ByteArray,
        val mimeType : String,
        val tags: Map<String, String> = mapOf()) {
}