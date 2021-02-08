package com.siyamand.aws.dynamodb.core.monitoring

import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.AggregateMonitoringEntity
import com.siyamand.aws.dynamodb.core.resource.ResourceEntity
import com.siyamand.aws.dynamodb.core.workflow.WorkflowResult

interface MetadataService {
    fun getMonitoredTables(): List<ResourceEntity>
    suspend fun startWorkflow(sourceTableName: String, workflowName: String, entity : AggregateMonitoringEntity): WorkflowResult
}