package com.siyamand.aws.dynamodb.core.sdk.appconfig.entities

class HostedConfigurationVersionEntity(
        val applicationId: String,
        val configurationProfileId: String,
        val description: String,
        val contentType: String,
        val latestVersionNumber: Int?
)