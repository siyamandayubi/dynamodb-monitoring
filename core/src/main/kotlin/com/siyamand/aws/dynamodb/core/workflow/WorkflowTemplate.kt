package com.siyamand.aws.dynamodb.core.workflow

interface WorkflowTemplate {
    val name: String
    val version: Int
    suspend fun getSteps(workflowContext: WorkflowContext): List<WorkflowStepInstance>
    fun getRequiredParameters(): List<RequiredWorkflowParameter>
}

data class RequiredWorkflowParameter(val name: String, val type: WorkflowParameterType)

enum class WorkflowParameterType {
    STRING,
    JSON,
    INT
}

class EmptyWorkflow : WorkflowTemplate {
    override val name: String = "Empty"
    override val version: Int = 1
    override suspend fun getSteps(workflowContext: WorkflowContext): List<WorkflowStepInstance> {
        return listOf()
    }

    override fun getRequiredParameters(): List<RequiredWorkflowParameter> {
        return listOf()
    }
}