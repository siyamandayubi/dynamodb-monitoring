package com.siyamand.aws.dynamodb.core.sdk.appconfig.entities

import java.time.Instant

class StartDeploymentEntity(
        val applicationId: String,
        val environmentId: String,
        val deploymentStrategyId: String,
        val configurationProfileId: String,
        val configurationVersion: String,
        val description: String,
        val tags: Map<String, String>)
