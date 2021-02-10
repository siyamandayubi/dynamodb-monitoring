package com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring

import kotlinx.serialization.Serializable

@Serializable
class AggregateMonitoringEntity {
        var databaseName: String = ""
        var databaseInstanceArn: String = ""
        val fields: MutableList<AggregateFieldEntity> = mutableListOf()
}