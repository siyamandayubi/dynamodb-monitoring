package com.siyamand.aws.dynamodb.core.workflow

interface WorkflowManager {
    suspend fun execute(instance: WorkflowInstance, workflowPersister: WorkflowPersister? = null): WorkflowResult
}