package com.siyamand.aws.dynamodb.core.workflow

import java.util.*

class WorkflowBuilderImpl(private val templates: Iterable<WorkflowTemplate>) : WorkflowBuilder {
    override suspend fun create(type: String, initialParams: Map<String, String>): WorkflowInstance {
        val template = templates.firstOrNull { it.name == type }
                ?: throw  Exception("No templates has been found '${type}'")

        val requiredParameters = template.getRequiredParameters()
        val notProvidedParams =requiredParameters.filter { !initialParams.containsKey(it.name) }
        if(notProvidedParams.any()){
            throw Exception("The following parameters are not provided. ${notProvidedParams.joinToString()}")
        }

        val context = WorkflowContext(initialParams.toMutableMap())

        return WorkflowInstance(
                UUID.randomUUID().toString(),
                context,
                template.getSteps(),
                0,
                null,
                template
        )
    }
}