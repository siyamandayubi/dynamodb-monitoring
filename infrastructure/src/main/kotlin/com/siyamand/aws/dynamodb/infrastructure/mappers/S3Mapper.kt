package com.siyamand.aws.dynamodb.infrastructure.mappers

import com.siyamand.aws.dynamodb.core.s3.CreateBucketRequestEntity
import software.amazon.awssdk.services.s3.model.CreateBucketRequest

class S3Mapper {
    companion object{
        fun convert(entity: CreateBucketRequestEntity): CreateBucketRequest{
            return CreateBucketRequest
                    .builder()
                    .bucket(entity.name)
                    .build()
        }
    }
}