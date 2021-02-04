package com.siyamand.aws.dynamodb.core.workflow

import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.MonitoringBaseEntity

interface WorkflowBuilder {
    fun create(type: String, initialParams: Map<String, Any>): WorkflowInstance
    fun build(monitoringBaseEntity: MonitoringBaseEntity<Any>): WorkflowInstance
    fun  serialize(workflowInstance: WorkflowInstance): String
}