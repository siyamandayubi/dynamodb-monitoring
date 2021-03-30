package com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring

import com.siyamand.aws.dynamodb.core.common.InstantSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
class AggregateFieldEntity {
    var name: String = ""
    var path: MutableList<String> = mutableListOf()

    @Serializable(InstantSerializer::class)
    var from: Instant = Instant.now()
}