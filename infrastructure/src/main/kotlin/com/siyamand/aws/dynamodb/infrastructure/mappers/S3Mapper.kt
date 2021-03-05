package com.siyamand.aws.dynamodb.infrastructure.mappers

import com.siyamand.aws.dynamodb.core.sdk.s3.CreateBucketRequestEntity
import com.siyamand.aws.dynamodb.core.sdk.s3.S3ObjectVersionEntity
import software.amazon.awssdk.services.s3.model.CreateBucketRequest
import software.amazon.awssdk.services.s3.model.ObjectVersion

class S3Mapper {
    companion object {
        fun convert(entity: CreateBucketRequestEntity): CreateBucketRequest {
            return CreateBucketRequest
                    .builder()
                    .bucket(entity.name)
                    .build()
        }

        fun convert(version: ObjectVersion): S3ObjectVersionEntity {
            return S3ObjectVersionEntity(
                    version.eTag(),
                    version.size() ?: 0,
                    version.storageClassAsString() ?: "",
                    version.key(),
                    version.versionId() ?: "",
                    version.isLatest,
                    version.lastModified())

        }
    }
}