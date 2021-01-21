package com.siyamand.aws.dynamodb.core.entities.s3

class CreateS3ObjectRequestEntity(val bucket: String,val key: String,val data: ByteArray) {
}