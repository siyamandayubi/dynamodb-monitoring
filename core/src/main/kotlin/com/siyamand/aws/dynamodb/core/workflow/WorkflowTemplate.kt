package com.siyamand.aws.dynamodb.core.workflow

interface WorkflowTemplate {
    val name: String
    val version: Int
    val steps: List<WorkflowStep>
}