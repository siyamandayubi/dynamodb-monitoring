package com.siyamand.aws.dynamodb.core.workflow

import java.util.*

class WorkflowBuilderImpl(private val templates: Iterable<WorkflowTemplate>) : WorkflowBuilder {
    override suspend fun create(type: String, initialParams: Map<String, String>): WorkflowInstance {
        val template = templates.firstOrNull { it.name == type }
                ?: throw  Exception("No templates has been found '${type}'")

        val context = WorkflowContext(initialParams.toMutableMap())

        return WorkflowInstance(
                UUID.randomUUID().toString(),
                context,

                template.steps.map {
                    it.initialize()
                    WorkflowStepInstance(it.name,WorkflowStepStatus.INITIAL, mapOf()) },
                0,
                null,
                template
        )
    }
}