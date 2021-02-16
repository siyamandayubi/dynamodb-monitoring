package com.siyamand.aws.dynamodb.core.monitoring

import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.AggregateMonitoringEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.MonitoringBaseEntity
import com.siyamand.aws.dynamodb.core.resource.ResourceEntity
import com.siyamand.aws.dynamodb.core.workflow.WorkflowResult

interface MetadataService {
    suspend fun getMonitoredTables(): List<MonitoringBaseEntity<AggregateMonitoringEntity>>
    suspend fun startWorkflow(sourceTableName: String, workflowName: String, entity : AggregateMonitoringEntity)
    suspend fun resumeWorkflow(id: String)
}