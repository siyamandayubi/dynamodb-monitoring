package com.siyamand.aws.dynamodb.core.monitoring

import com.siyamand.aws.dynamodb.core.dynamodb.TableItemEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.AggregateMonitoringEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.MonitoringBaseEntity

interface MonitoringItemConverter {
    fun convertToAggregateEntity(tableItemEntity: TableItemEntity): MonitoringBaseEntity<AggregateMonitoringEntity>
    fun convert(tableName: String, monitoringBaseEntity: MonitoringBaseEntity<AggregateMonitoringEntity>): TableItemEntity
}