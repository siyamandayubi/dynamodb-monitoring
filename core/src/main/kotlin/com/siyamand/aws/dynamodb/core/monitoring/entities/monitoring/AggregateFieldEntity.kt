package com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring

import com.siyamand.aws.dynamodb.core.common.InstantSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
class AggregateFieldEntity(
        val name: String,
        val path: String,
        @Serializable(InstantSerializer::class)
        val from: Instant) {
}