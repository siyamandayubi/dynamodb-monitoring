package com.siyamand.aws.dynamodb.core.sdk.rds.entities

import com.siyamand.aws.dynamodb.core.sdk.resource.ResourceEntity

class DbProxyTargetGroupEntity(
        val groupName: String,
        val isDefault: Boolean,
        val resource: ResourceEntity
)