package com.siyamand.aws.dynamodb.core.workflow.defaultSteps

import com.siyamand.aws.dynamodb.core.template.TemplateEngine
import com.siyamand.aws.dynamodb.core.workflow.WorkflowInstance
import com.siyamand.aws.dynamodb.core.workflow.WorkflowResult
import com.siyamand.aws.dynamodb.core.workflow.WorkflowResultType
import com.siyamand.aws.dynamodb.core.workflow.WorkflowStep
import kotlin.contracts.contract

class AssignWorkflowStep(private val templateEngine: TemplateEngine) : WorkflowStep() {
    override val name: String = "Assign"

    override suspend fun execute(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {
        if (params.containsKey("newValue")) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "'newValue' field is not provided")
        }
        if (params.containsKey("variable")) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "'variable' field is not provided")
        }

        val variable = params["variable"]!!
        instance.context.sharedData[variable] = templateEngine.execute(params["newValue"]!!, instance.context.sharedData.mapValues { it.value as Any })

        return WorkflowResult(WorkflowResultType.SUCCESS, mapOf(), "")
    }

    override suspend fun isWaiting(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {
        return execute(instance, owner, params)
    }

    override suspend fun initialize() {
    }
}