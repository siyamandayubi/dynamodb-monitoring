package com.siyamand.aws.dynamodb.core.workflow

interface WorkflowManager {
    suspend fun execute(instance: WorkflowInstance, owner: Any, workflowPersister: WorkflowPersister? = null): WorkflowResult
}