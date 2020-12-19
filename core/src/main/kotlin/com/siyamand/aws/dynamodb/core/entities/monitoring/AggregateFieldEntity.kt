package com.siyamand.aws.dynamodb.core.entities.monitoring

import java.time.Instant

class AggregateFieldEntity(val name: String, val path: String, val from: Instant, aggregateType: AggregateType) {
}