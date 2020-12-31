package com.siyamand.aws.dynamodb.core.services

import com.siyamand.aws.dynamodb.core.entities.ResourceEntity

interface MetadataService {
    fun getMonitoredTables(): List<ResourceEntity>
}