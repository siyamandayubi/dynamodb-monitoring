package com.siyamand.aws.dynamodb.core.sdk.appconfig.entities

import java.time.Instant

class DeploymentStatusEntity(
        val deploymentNumber: Int,
        val description: String,
        val state: String,
        val eventLog: List<DeploymentEventEntity>,
        val percentageComplete: Float,
        val startedAt: Instant? = null,
        val completedAt: Instant? = null

)

class DeploymentEventEntity(
        val eventType: String? = null,
        val triggeredBy: String? = null,
        val description: String? = null,
        val occurredAt: Instant? = null)
