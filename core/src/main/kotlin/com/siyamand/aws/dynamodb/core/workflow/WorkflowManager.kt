package com.siyamand.aws.dynamodb.core.workflow

interface WorkflowManager {
    suspend fun execute(instance: WorkflowInstance): WorkflowResult
    fun setWorkflowPersister(workflowPersister: WorkflowPersister)
}