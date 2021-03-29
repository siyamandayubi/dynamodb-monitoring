package com.siyamand.aws.dynamodb.core.workflow.defaultSteps

import com.siyamand.aws.dynamodb.core.workflow.WorkflowInstance
import com.siyamand.aws.dynamodb.core.workflow.WorkflowResult
import com.siyamand.aws.dynamodb.core.workflow.WorkflowResultType
import com.siyamand.aws.dynamodb.core.workflow.WorkflowStep

class JumpWorkflowStep() : WorkflowStep() {
    override val name: String = "Jump"

    override suspend fun execute(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {
        if (!params.containsKey("target")) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "'target' field is not provided")
        }

        return WorkflowResult(WorkflowResultType.SUCCESS, mapOf(), "", params["target"]!!)
    }

    override suspend fun isWaiting(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {
        return execute(instance, owner, params)
    }

    override suspend fun initialize() {
    }
}