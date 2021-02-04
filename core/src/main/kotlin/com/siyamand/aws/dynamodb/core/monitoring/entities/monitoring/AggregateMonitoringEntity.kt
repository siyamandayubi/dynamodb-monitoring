package com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring

class AggregateMonitoringEntity(
        val databaseName: String,
        val databaseInstanceArn: String,
        val fields: List<AggregateFieldEntity>) {
}