package com.siyamand.aws.dynamodb.core.sdk.s3

import java.time.Instant

class  S3ObjectVersionEntity(
     val eTag: String,
     val size: Long,
     val storageClass: String,
     val key: String,
     val versionId: String,
     val isLatest: Boolean,
     val lastModified: Instant
)