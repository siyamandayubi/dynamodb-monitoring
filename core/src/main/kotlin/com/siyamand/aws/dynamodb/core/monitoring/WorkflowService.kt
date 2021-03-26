package com.siyamand.aws.dynamodb.core.monitoring

import com.siyamand.aws.dynamodb.core.sdk.dynamodb.TableDetailEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.AggregateMonitoringEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.MonitoringBaseEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.StartMonitoringWorkflowEntity

interface WorkflowService {
    suspend fun startWorkflow(model: StartMonitoringWorkflowEntity)
    suspend fun resumeWorkflow(id: String)
}