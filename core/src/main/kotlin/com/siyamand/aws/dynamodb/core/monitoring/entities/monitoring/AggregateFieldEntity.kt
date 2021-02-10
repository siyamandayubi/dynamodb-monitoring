package com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
class AggregateFieldEntity(
        val name: String,
        val path: String,
        @Contextual
        val from: Instant,
        val tableName: String,
        val aggregateType: AggregateType) {
}