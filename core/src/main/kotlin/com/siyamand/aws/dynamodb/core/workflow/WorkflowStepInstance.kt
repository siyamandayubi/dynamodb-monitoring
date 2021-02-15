package com.siyamand.aws.dynamodb.core.workflow

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class WorkflowStepInstance(
        val name: String,
        @Transient
        val step: WorkflowStep = EmptyWorkflowStep(),
        var status: WorkflowStepStatus = WorkflowStepStatus.INITIAL,
        val params: Map<String, String>
) {
    private class EmptyWorkflowStep : WorkflowStep() {
        override val name: String = "Empty"

        override suspend fun execute(context: WorkflowContext, params: Map<String, String>): WorkflowResult {
            return WorkflowResult(WorkflowResultType.SUCCESS, mapOf(), "")
        }

        override suspend fun isWaiting(context: WorkflowContext, params: Map<String, String>): WorkflowResult {
            return WorkflowResult(WorkflowResultType.SUCCESS, mapOf(), "")
        }

        override suspend fun initialize() {
        }

    }

}

enum class WorkflowStepStatus {
    INITIAL,
    STARTING,
    WAITING,
    FINISHED
}