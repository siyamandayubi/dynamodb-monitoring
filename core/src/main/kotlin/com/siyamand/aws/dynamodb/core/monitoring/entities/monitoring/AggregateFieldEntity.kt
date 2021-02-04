package com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring

import java.time.Instant

class AggregateFieldEntity(
        val name: String,
        val path: String,
        val from: Instant,
        val tableName: String,
        aggregateType: AggregateType) {
}