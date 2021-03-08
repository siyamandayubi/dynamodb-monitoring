package com.siyamand.aws.dynamodb.core.workflow

import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.AggregateMonitoringEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.MonitoringBaseEntity

interface WorkflowConverter {
    suspend fun  build(monitoringBaseEntity: MonitoringBaseEntity<AggregateMonitoringEntity>, workflowString: String): WorkflowInstance
    fun  serialize(workflowInstance: WorkflowInstance): String
}