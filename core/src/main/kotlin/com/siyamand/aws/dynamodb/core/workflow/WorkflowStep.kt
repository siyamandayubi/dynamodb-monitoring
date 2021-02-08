package com.siyamand.aws.dynamodb.core.workflow

abstract class WorkflowStep {
    abstract val name : String
    abstract suspend fun execute(context: WorkflowContext, params: Map<String, Any>): WorkflowResult
    abstract suspend fun isWaiting(context: WorkflowContext, params: Map<String, Any>): WorkflowResult
}