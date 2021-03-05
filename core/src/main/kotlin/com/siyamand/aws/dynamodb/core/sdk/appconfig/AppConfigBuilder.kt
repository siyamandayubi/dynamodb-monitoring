package com.siyamand.aws.dynamodb.core.sdk.appconfig

import com.siyamand.aws.dynamodb.core.sdk.appconfig.entities.*

interface AppConfigBuilder {
    fun build(name: String, monitoringId: String): CreateApplicationEntity
    fun build(applicationId: String, name: String, monitoringId: String): CreateEnvironmentEntity
    fun buildDeploymentStrategy(name: String, monitoringId: String): CreateDeploymentStrategyEntity
    fun buildProfile(applicationId: String, name: String, locationUri: String, roleArn: String, monitoringId: String): CreateConfigurationProfileEntity
    fun buildDeployment(
            applicationId: String,
            environmentId: String,
            deploymentStragegyId: String,
            configurationProfileId: String,
            configurationVersion: String,
            monitoringId: String): StartDeploymentEntity

    fun buildHostedConfiguration(applicationId: String, configurationProfileId: String, content: String): CreateHostedConfigurationVersionEntity
}