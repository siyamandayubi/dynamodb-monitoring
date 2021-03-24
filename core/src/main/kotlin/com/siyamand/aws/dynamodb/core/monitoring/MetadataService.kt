package com.siyamand.aws.dynamodb.core.monitoring

import com.siyamand.aws.dynamodb.core.common.PageResultEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.AggregateMonitoringEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.MonitoringBaseEntity

interface MetadataService {
    suspend fun getMonitoredTables(startKey: String): PageResultEntity<MonitoringBaseEntity<AggregateMonitoringEntity>>
}