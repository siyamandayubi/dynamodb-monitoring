package com.siyamand.aws.dynamodb.core.workflow

class WorkflowInstance(
        val id: String,
        val context: WorkflowContext,
        val template: WorkflowTemplate,
        val steps: List<WorkflowStepInstance>,
        val currentStep: Int,
        val lastResult: WorkflowResult?) {
}