package com.siyamand.aws.dynamodb.core.workflow

interface WorkflowTemplate {
    val name: String
    val steps: List<WorkflowStepInstance>
}