package com.siyamand.aws.dynamodb.core.sdk.appconfig.entities

class ConfigurationProfileEntity(
        val id: String,
        val applicationId: String,
        val name: String,
        val description: String,
        val locationUri: String,
        val retrievalRoleArn: String) {
    val tags: MutableMap<String, String> = mutableMapOf()
}