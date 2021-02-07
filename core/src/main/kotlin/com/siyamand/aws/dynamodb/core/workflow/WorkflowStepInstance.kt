package com.siyamand.aws.dynamodb.core.workflow
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
class WorkflowStepInstance(
        val name: String,
        var status: WorkflowStepStatus = WorkflowStepStatus.INITIAL,
        val params: Map<String, String>
)

enum class WorkflowStepStatus {
    INITIAL,
    STARTING,
    WAITING,
    FINISHED
}