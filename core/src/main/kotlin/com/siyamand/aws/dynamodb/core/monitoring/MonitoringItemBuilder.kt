package com.siyamand.aws.dynamodb.core.monitoring

import com.siyamand.aws.dynamodb.core.dynamodb.TableItemEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.MonitoringBaseEntity

interface MonitoringItemBuilder {
    fun build(tableItemEntity: TableItemEntity): MonitoringBaseEntity<Any>
    fun convert(monitoringBaseEntity: MonitoringBaseEntity<Any>): TableItemEntity
}