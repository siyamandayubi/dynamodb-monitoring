package com.siyamand.aws.dynamodb.core.sdk.appconfig.entities

class CreateConfigurationProfileEntity(
        val applicationId: String,
        val name: String,
        val description: String,
        val locationUri: String,
        val retrievalRoleArn: String,
        val tags: Map<String, String>
)