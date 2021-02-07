package com.siyamand.aws.dynamodb.core.workflow

import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.AggregateMonitoringEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.MonitoringBaseEntity

interface WorkflowBuilder {
    fun create(type: String, initialParams: Map<String, Any>): WorkflowInstance
    fun build(monitoringBaseEntity: MonitoringBaseEntity<AggregateMonitoringEntity>): WorkflowInstance
    fun  serialize(workflowInstance: WorkflowInstance): String
}