package com.siyamand.aws.dynamodb.core.monitoring

import com.siyamand.aws.dynamodb.core.resource.ResourceEntity

interface MetadataService {
    fun getMonitoredTables(): List<ResourceEntity>
}