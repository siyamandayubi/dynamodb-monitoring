package com.siyamand.aws.dynamodb.core.workflow

import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.AggregateMonitoringEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.MonitoringBaseEntity

interface WorkflowConverter {
    fun build(monitoringBaseEntity: MonitoringBaseEntity<AggregateMonitoringEntity>): WorkflowInstance
    fun  serialize(workflowInstance: WorkflowInstance): String
}