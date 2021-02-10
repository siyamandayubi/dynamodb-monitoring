package com.siyamand.aws.dynamodb.core.workflow

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class WorkflowInstance(
        val id: String,
        val context: WorkflowContext,
        val steps: List<WorkflowStepInstance>,
        val currentStep: Int,
        val lastResult: WorkflowResult?,
        @Transient
        var template: WorkflowTemplate = EmptyWorkflow()) {
}