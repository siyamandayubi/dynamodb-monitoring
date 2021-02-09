package com.siyamand.aws.dynamodb.core.schedule

interface WorkflowJobHandler {
    suspend fun execute()
}