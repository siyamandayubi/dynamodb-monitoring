package com.siyamand.aws.dynamodb.core.workflow

interface WorkflowBuilder {
    suspend fun create(type: String, initialParams: Map<String, String>): WorkflowInstance
}