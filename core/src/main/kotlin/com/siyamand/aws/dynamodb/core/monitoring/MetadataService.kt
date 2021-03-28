package com.siyamand.aws.dynamodb.core.monitoring

import com.siyamand.aws.dynamodb.core.common.PageResultEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.AggregateMonitoringEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.MonitoringBaseEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.MonitoringResourceEntity

interface MetadataService {
    suspend fun getMonitoringRecords(startKey: String): PageResultEntity<MonitoringBaseEntity<AggregateMonitoringEntity>>
    suspend fun getMonitoringItemResources(id: String): List<MonitoringResourceEntity>
    suspend fun getMonitoringRecord(id: String): MonitoringBaseEntity<AggregateMonitoringEntity>?
}