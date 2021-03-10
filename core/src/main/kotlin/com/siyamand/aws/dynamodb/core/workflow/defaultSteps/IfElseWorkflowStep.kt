package com.siyamand.aws.dynamodb.core.workflow.defaultSteps

import com.siyamand.aws.dynamodb.core.template.TemplateEngine
import com.siyamand.aws.dynamodb.core.workflow.WorkflowInstance
import com.siyamand.aws.dynamodb.core.workflow.WorkflowResult
import com.siyamand.aws.dynamodb.core.workflow.WorkflowResultType
import com.siyamand.aws.dynamodb.core.workflow.WorkflowStep
import kotlin.contracts.contract

class IfElseWorkflowStep(private val templateEngine: TemplateEngine) : WorkflowStep() {
    override val name: String = "IfElse"

    override suspend fun execute(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {
        if (params.containsKey("condition")) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "'condition' field is not provided")
        }
        if (params.containsKey("else")) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "'else' field is not provided")
        }

        val resultStr = templateEngine.execute(params["condition"]!!, instance.context.sharedData.mapValues { it.value as Any })
        val result = try {
            resultStr.toBoolean()
        } catch (ex: Exception) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "$resultStr can not be cast to boolena")
        }

        val nextStep = if (result) "" else params["else"]!!

        return WorkflowResult(WorkflowResultType.SUCCESS, mapOf(), "", nextStep)
    }

    override suspend fun isWaiting(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {
        return execute(instance, owner, params)
    }

    override suspend fun initialize() {
    }
}