package com.siyamand.aws.dynamodb.core.workflow

import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.MonitoringBaseEntity

class WorkflowBuilderImpl : WorkflowBuilder {
    override fun create(type: String, initialParams: Map<String, Any>): WorkflowInstance {
        TODO("Not yet implemented")
    }

    override fun build(monitoringBaseEntity: MonitoringBaseEntity<Any>):WorkflowInstance{
        TODO()
    }

    override fun  serialize(workflowInstance: WorkflowInstance): String{
        TODO()
    }
}