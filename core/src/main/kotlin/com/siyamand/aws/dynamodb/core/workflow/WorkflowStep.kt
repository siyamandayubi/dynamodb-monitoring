package com.siyamand.aws.dynamodb.core.workflow

abstract class WorkflowStep {
    abstract val name: String
    abstract suspend fun execute(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult
    abstract suspend fun isWaiting(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult
    abstract suspend fun initialize()
}