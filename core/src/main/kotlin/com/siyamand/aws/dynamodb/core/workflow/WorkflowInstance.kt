package com.siyamand.aws.dynamodb.core.workflow

import kotlinx.serialization.Serializable

@Serializable
class WorkflowInstance(
        val id: String,
        val context: WorkflowContext,
        @Transient
        var template: WorkflowTemplate,
        val steps: List<WorkflowStepInstance>,
        val currentStep: Int,
        val lastResult: WorkflowResult?) {
}