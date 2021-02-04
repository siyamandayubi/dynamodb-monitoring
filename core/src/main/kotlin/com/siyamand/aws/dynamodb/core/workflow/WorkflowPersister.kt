package com.siyamand.aws.dynamodb.core.workflow

interface WorkflowPersister {
    suspend fun load(id: String): WorkflowInstance
    suspend fun save(instance: WorkflowInstance)
}