package com.siyamand.aws.dynamodb.web.models

import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.AggregateMonitoringEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.MonitoringBaseEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.MonitoringResourceEntity

class MonitoringDetailModel(val detail: MonitoringBaseEntity<AggregateMonitoringEntity>, val resources: List<MonitoringResourceEntity>) {
}