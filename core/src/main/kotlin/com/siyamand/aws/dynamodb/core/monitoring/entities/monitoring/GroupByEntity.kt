package com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring

import kotlinx.serialization.Serializable

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