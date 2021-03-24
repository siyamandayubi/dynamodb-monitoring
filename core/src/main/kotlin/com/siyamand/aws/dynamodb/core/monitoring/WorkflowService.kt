package com.siyamand.aws.dynamodb.core.monitoring

import com.siyamand.aws.dynamodb.core.sdk.dynamodb.TableDetailEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.AggregateMonitoringEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.MonitoringBaseEntity

interface WorkflowService {
    suspend fun startWorkflow(sourceTableName: String, workflowName: String, entity : AggregateMonitoringEntity)
    suspend fun resumeWorkflow(id: String)
}