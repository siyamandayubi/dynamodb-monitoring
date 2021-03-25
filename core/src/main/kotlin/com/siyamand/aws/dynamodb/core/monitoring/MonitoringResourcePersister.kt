package com.siyamand.aws.dynamodb.core.monitoring

interface MonitoringResourcePersister {
    suspend fun threadSafe()
    suspend fun persist(monitoringId: String, arn: String)
}