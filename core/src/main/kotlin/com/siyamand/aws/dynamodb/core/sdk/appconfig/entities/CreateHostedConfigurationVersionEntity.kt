package com.siyamand.aws.dynamodb.core.sdk.appconfig.entities

class CreateHostedConfigurationVersionEntity(
        val applicationId: String,
        val configurationProfileId: String,
        val description: String,
        val content: ByteArray,
        val contentType: String,
        val latestVersionNumber: Int?
)