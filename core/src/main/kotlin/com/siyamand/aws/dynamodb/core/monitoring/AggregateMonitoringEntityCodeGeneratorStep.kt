package com.siyamand.aws.dynamodb.core.monitoring

import com.siyamand.aws.dynamodb.core.template.TemplateEngine
import com.siyamand.aws.dynamodb.core.workflow.WorkflowContext
import com.siyamand.aws.dynamodb.core.workflow.WorkflowInstance
import com.siyamand.aws.dynamodb.core.workflow.WorkflowResult
import com.siyamand.aws.dynamodb.core.workflow.WorkflowStep

class AggregateMonitoringEntityCodeGeneratorStep(private val templateEngine: TemplateEngine) : WorkflowStep() {
    override val name: String = "AggregateMonitoringEntityCodeGenerator"

    override suspend fun execute(instance: WorkflowInstance, context: WorkflowContext, params: Map<String, String>): WorkflowResult {
        TODO("Not yet implemented")
    }

    override suspend fun isWaiting(instance: WorkflowInstance, context: WorkflowContext, params: Map<String, String>): WorkflowResult {
        TODO("Not yet implemented")
    }

    override suspend fun initialize() {
        TODO("Not yet implemented")
    }
}