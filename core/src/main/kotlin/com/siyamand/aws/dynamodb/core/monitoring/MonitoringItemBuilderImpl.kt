package com.siyamand.aws.dynamodb.core.monitoring

import com.siyamand.aws.dynamodb.core.dynamodb.TableItemEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.AggregateMonitoringEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.MonitoringBaseEntity

class MonitoringItemBuilderImpl : MonitoringItemBuilder {
    override fun build(tableItemEntity: TableItemEntity): MonitoringBaseEntity<Any> {
        TODO()
    }

    override fun convert(monitoringBaseEntity: MonitoringBaseEntity<Any>): TableItemEntity{
        TODO()
    }
}