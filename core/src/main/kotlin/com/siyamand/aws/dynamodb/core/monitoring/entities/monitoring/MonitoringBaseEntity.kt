package com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring

class MonitoringBaseEntity<T>(
        val id: String,
        val sourceTable: String,
        val type :String,
        val status: MonitorStatus,
        val version: Int,
        var workflow: String,
        val relatedData: T) {
}