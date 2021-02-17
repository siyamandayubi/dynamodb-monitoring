package com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring

import kotlinx.serialization.Serializable

@Serializable
class AggregateMonitoringEntity {
    var databaseName: String = ""
    var databaseInstanceArn: String = ""
    val groups: MutableList<GroupByEntity> = mutableListOf()
}

@Serializable
class GroupByEntity {
    var fieldName: String = ""
    var tableName: String = ""

    /// Only for numeric fields
    var startingFrom: Long = 0

    // Only for numeric fields
    var period: Long = 100

    val fields: MutableList<AggregateFieldEntity> = mutableListOf()
}