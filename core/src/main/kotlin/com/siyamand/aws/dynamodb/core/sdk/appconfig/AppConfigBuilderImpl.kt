package com.siyamand.aws.dynamodb.core.sdk.appconfig

import com.siyamand.aws.dynamodb.core.common.MonitorConfigProvider
import com.siyamand.aws.dynamodb.core.sdk.appconfig.entities.*
import java.nio.charset.StandardCharsets

class AppConfigBuilderImpl(private val monitorConfigProvider: MonitorConfigProvider) : AppConfigBuilder {
    override fun build(name: String, monitoringId: String): CreateApplicationEntity {
        return CreateApplicationEntity(
                name,
                "",
                mapOf(
                        monitorConfigProvider.getMonitoringVersionTagName() to monitorConfigProvider.getMonitoringVersionValue(),
                        monitorConfigProvider.getMonitoringMetadataIdTagName() to monitoringId))
    }

    override fun build(applicationId: String, name: String, monitoringId: String): CreateEnvironmentEntity {
        return CreateEnvironmentEntity(
                applicationId,
                name,
                "",
                mapOf(
                        monitorConfigProvider.getMonitoringVersionTagName() to monitorConfigProvider.getMonitoringVersionValue(),
                        monitorConfigProvider.getMonitoringMetadataIdTagName() to monitoringId))
    }

    override fun buildDeploymentStrategy(name: String, monitoringId: String): CreateDeploymentStrategyEntity {
        return CreateDeploymentStrategyEntity(
                name,
                "",
                1,
                1,
                20.0F,
                "Linear",
                "NONE",
                mapOf(
                        monitorConfigProvider.getMonitoringVersionTagName() to monitorConfigProvider.getMonitoringVersionValue(),
                        monitorConfigProvider.getMonitoringMetadataIdTagName() to monitoringId)
        )
    }


    override fun buildProfile(applicationId: String, name: String, locationUri: String, roleArn: String, monitoringId: String): CreateConfigurationProfileEntity {
        return CreateConfigurationProfileEntity(
                applicationId,
                name,
                "",
                locationUri,
                roleArn,
                mapOf(
                        monitorConfigProvider.getMonitoringVersionTagName() to monitorConfigProvider.getMonitoringVersionValue(),
                        monitorConfigProvider.getMonitoringMetadataIdTagName() to monitoringId)
        )
    }

    override fun buildHostedConfiguration(applicationId: String, configurationProfileId: String, content: String): CreateHostedConfigurationVersionEntity {
        return CreateHostedConfigurationVersionEntity(
                applicationId,
                configurationProfileId,
                "",
                content.toByteArray(StandardCharsets.UTF_8),
                "text/JSON",
                null
        )
    }

    override fun buildDeployment(
            applicationId: String,
            environmentId: String,
            deploymentStragegyId: String,
            configurationProfileId: String,
            configurationVersion: String,
            monitoringId: String): StartDeploymentEntity {
        return StartDeploymentEntity(
                applicationId,
                environmentId,
                deploymentStragegyId,
                configurationProfileId,
                configurationVersion,
                "",
                mapOf(
                        monitorConfigProvider.getMonitoringVersionTagName() to monitorConfigProvider.getMonitoringVersionValue(),
                        monitorConfigProvider.getMonitoringMetadataIdTagName() to monitoringId)

        )
    }
}