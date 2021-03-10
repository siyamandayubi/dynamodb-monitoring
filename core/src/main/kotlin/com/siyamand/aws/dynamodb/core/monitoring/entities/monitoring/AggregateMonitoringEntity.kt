package com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring

import kotlinx.serialization.Serializable

@Serializable
class AggregateMonitoringEntity {
    var databaseName: String = ""
    var databaseInstanceArn: String = ""
    var instancesCount: Int = 1
    val groups: MutableList<GroupByEntity> = mutableListOf()
}

