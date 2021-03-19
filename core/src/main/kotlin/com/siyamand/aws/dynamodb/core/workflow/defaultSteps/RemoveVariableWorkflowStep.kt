package com.siyamand.aws.dynamodb.core.workflow.defaultSteps

import com.siyamand.aws.dynamodb.core.template.TemplateEngine
import com.siyamand.aws.dynamodb.core.workflow.WorkflowInstance
import com.siyamand.aws.dynamodb.core.workflow.WorkflowResult
import com.siyamand.aws.dynamodb.core.workflow.WorkflowResultType
import com.siyamand.aws.dynamodb.core.workflow.WorkflowStep
import kotlin.contracts.contract

class RemoveVariableWorkflowStep : WorkflowStep() {
    override val name: String = "Remove"

    override suspend fun execute(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {
        if (!params.containsKey("variable")) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "'variable' field is not provided")
        }

        val variables = params["variable"]!!.split(',').map { it.trim(' ', '\n') }

        variables.forEach { variable ->
            if (instance.context.sharedData.containsKey(variable)) {
                instance.context.sharedData.remove(variable)
            }
        }
        return WorkflowResult(WorkflowResultType.SUCCESS, mapOf(), "")
    }

    override suspend fun isWaiting(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {
        return execute(instance, owner, params)
    }

    override suspend fun initialize() {
    }
}