package com.siyamand.aws.dynamodb.core.services

import com.siyamand.aws.dynamodb.core.entities.monitoring.MonitoringMetadataEntity

interface MetadataService {
    suspend fun create()
    suspend fun load(): MonitoringMetadataEntity
}