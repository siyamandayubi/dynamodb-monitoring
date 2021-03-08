package com.siyamand.aws.dynamodb.core.sdk.rds.entities

import com.siyamand.aws.dynamodb.core.sdk.resource.ResourceEntity

class DbProxyTargetEntity(
        val targetResource: ResourceEntity?,
        val endpoint: String,
        val trackedClusterId: String?,
        val rdsResourceId: String?,
        val port: Int,
        val type: String?
)