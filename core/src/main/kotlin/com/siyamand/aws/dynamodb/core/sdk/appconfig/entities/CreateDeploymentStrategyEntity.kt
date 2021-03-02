package com.siyamand.aws.dynamodb.core.sdk.appconfig.entities

class CreateDeploymentStrategyEntity(
        val name: String,
        val description: String,

        val deploymentDurationInMinutes: Int?,

        val finalBakeTimeInMinutes: Int?,

        val growthFactor: Float?,

        val growthType: String?,

        val replicateTo: String?,
        val tags: Map<String, String>)