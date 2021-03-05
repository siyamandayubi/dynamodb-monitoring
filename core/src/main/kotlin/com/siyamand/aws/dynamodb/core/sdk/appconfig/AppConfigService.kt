package com.siyamand.aws.dynamodb.core.sdk.appconfig

interface AppConfigService {
    suspend fun createConfig(
            applicationName: String,
            environmentName: String,
            deploymentStrategyName: String,
            profileName: String,
            content: Map<String, String>)
}