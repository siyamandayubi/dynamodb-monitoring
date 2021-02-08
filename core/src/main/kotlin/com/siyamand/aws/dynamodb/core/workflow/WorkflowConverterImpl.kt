package com.siyamand.aws.dynamodb.core.workflow

import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.AggregateMonitoringEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.MonitoringBaseEntity
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

class WorkflowConverterImpl(private val templates: Iterable<WorkflowTemplate>) : WorkflowConverter {
    override fun build(entity: MonitoringBaseEntity<AggregateMonitoringEntity>): WorkflowInstance {
        val template = templates.firstOrNull { it.name == entity.type }
                ?: throw  Exception("No templates has been found '${entity.type}'")

        val workflowInstance = Json.decodeFromString<WorkflowInstance>(entity.workflow)
        workflowInstance.template = template
        return workflowInstance
    }

    override fun serialize(workflowInstance: WorkflowInstance): String {
        return Json.encodeToString(workflowInstance)
    }
}