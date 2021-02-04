package com.siyamand.aws.dynamodb.core.workflow

class WorkflowStepInstance(
        val workflowStep: WorkflowStep,
        val name: String,
        var status: WorkflowStepStatus = WorkflowStepStatus.INITIAL,
        val params: Map<String, String>
) {

}

enum class WorkflowStepStatus {
    INITIAL,
    STARTING,
    WAITING,
    FINISHED
}