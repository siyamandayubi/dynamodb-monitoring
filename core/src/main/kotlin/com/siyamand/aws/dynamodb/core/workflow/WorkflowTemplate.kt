package com.siyamand.aws.dynamodb.core.workflow

interface WorkflowTemplate {
    val name: String
    val version: Int
    val steps: List<WorkflowStep>

    // Default params per step name
    val defaultParams: Map<String, Map<String, String>>
}

class EmptyWorkflow : WorkflowTemplate {
    override val name: String = "Empty"
    override val version: Int = 1
    override val steps: List<WorkflowStep> = listOf()
    override val defaultParams: Map<String, Map<String, String>> = mapOf()

}