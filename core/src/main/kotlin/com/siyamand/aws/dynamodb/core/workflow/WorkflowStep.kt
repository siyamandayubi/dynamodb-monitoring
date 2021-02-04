package com.siyamand.aws.dynamodb.core.workflow

abstract class WorkflowStep {
    abstract fun execute(context: WorkflowContext, params: Map<String, Any>): WorkflowResult
    abstract fun isWaiting(context: WorkflowContext, params: Map<String, Any>): WorkflowResult
}