package com.siyamand.aws.dynamodb.infrastructure.mappers

import com.siyamand.aws.dynamodb.core.sdk.appconfig.*
import com.siyamand.aws.dynamodb.core.sdk.appconfig.entities.*
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.services.appconfig.model.*

class AppConfigMapper {
    companion object {
        fun convert(entity: CreateApplicationEntity): CreateApplicationRequest {
            return CreateApplicationRequest
                    .builder()
                    .name(entity.name)
                    .description(entity.description)
                    .tags(entity.tags)
                    .build()
        }

        fun convert(entity: CreateEnvironmentEntity): CreateEnvironmentRequest {
            return CreateEnvironmentRequest
                    .builder()
                    .name(entity.name)
                    .description(entity.description)
                    .applicationId(entity.applicationId)
                    .tags(entity.tags)
                    .build()
        }

        fun convert(entity: CreateConfigurationProfileEntity): CreateConfigurationProfileRequest {
            return CreateConfigurationProfileRequest
                    .builder()
                    .applicationId(entity.applicationId)
                    .description(entity.description)
                    .name(entity.name)
                    .locationUri(entity.locationUri)
                    .retrievalRoleArn(entity.retrievalRoleArn)
                    .tags(entity.tags)
                    .build()
        }

        fun convert(entity: StartDeploymentEntity): StartDeploymentRequest {
            return StartDeploymentRequest
                    .builder()
                    .applicationId(entity.applicationId)
                    .description(entity.description)
                    .configurationProfileId(entity.configurationProfileId)
                    .configurationVersion(entity.configurationVersion)
                    .deploymentStrategyId(entity.deploymentStrategyId)
                    .environmentId(entity.environmentId)
                    .tags(entity.tags)
                    .build()
        }

        fun convert(entity: CreateHostedConfigurationVersionEntity): CreateHostedConfigurationVersionRequest {
            val builder = CreateHostedConfigurationVersionRequest
                    .builder()
                    .applicationId(entity.applicationId)
                    .description(entity.description)
                    .configurationProfileId(entity.configurationProfileId)
                    .content(SdkBytes.fromByteArray(entity.content))
                    .contentType(entity.contentType)

            if (entity.latestVersionNumber != null) {
                builder.latestVersionNumber(entity.latestVersionNumber)
            }
            return builder.build()
        }

        fun convert(entity: CreateDeploymentStrategyEntity): CreateDeploymentStrategyRequest {
            val builder = CreateDeploymentStrategyRequest
                    .builder()
                    .name(entity.name)
                    .description(entity.description)
                    .tags(entity.tags)
            if (entity.deploymentDurationInMinutes != null) {
                builder.deploymentDurationInMinutes(entity.deploymentDurationInMinutes)
            }

            if (entity.finalBakeTimeInMinutes != null) {
                builder.finalBakeTimeInMinutes(entity.finalBakeTimeInMinutes)
            }

            if (entity.growthFactor != null) {
                builder.growthFactor(entity.growthFactor)
            }

            if (entity.growthType != null) {
                builder.growthFactor(entity.growthFactor)
            }

            if (entity.replicateTo != null) {
                builder.replicateTo(entity.replicateTo)
            }
            return builder.build()
        }

        fun convert(response: CreateDeploymentStrategyResponse): DeploymentStrategyEntity {
            return DeploymentStrategyEntity(
                    response.id(),
                    response.name(),
                    response.description(),
                    response.deploymentDurationInMinutes(),
                    response.finalBakeTimeInMinutes(),
                    response.growthFactor(),
                    response.growthTypeAsString(),
                    response.replicateToAsString())
        }

        fun convert(response: DeploymentStrategy): DeploymentStrategyEntity {
            return DeploymentStrategyEntity(
                    response.id(),
                    response.name(),
                    response.description(),
                    response.deploymentDurationInMinutes(),
                    response.finalBakeTimeInMinutes(),
                    response.growthFactor(),
                    response.growthTypeAsString(),
                    response.replicateToAsString())
        }

        fun convert(response: CreateApplicationResponse): ApplicationEntity {
            return ApplicationEntity(response.id(), response.name(), response.description())
        }

        fun convert(response: Application): ApplicationEntity {
            return ApplicationEntity(response.id(), response.name(), response.description())
        }

        fun convert(response: Environment): EnvironmentEntity {
            return EnvironmentEntity(response.id(), response.applicationId(), response.stateAsString(), response.name(), response.description())
        }

        fun convert(response: CreateEnvironmentResponse): EnvironmentEntity {
            return EnvironmentEntity(response.id(), response.applicationId(), response.stateAsString(), response.name(), response.description())
        }

        fun convert(response: StartDeploymentResponse): DeploymentStatusEntity {
            return DeploymentStatusEntity(
                    response.applicationId(),
                    response.environmentId(),
                    response.deploymentStrategyId(),
                    response.configurationProfileId(),
                    response.deploymentNumber() ?: 0,
                    response.description() ?: "",
                    response.stateAsString() ?: "",
                    if (response.hasEventLog()) response.eventLog().map { convert(it) } else listOf(),
                    (response.percentageComplete() ?: 0.0) as Float,
                    response.startedAt(),
                    response.completedAt())
        }

        fun convert(response: GetDeploymentResponse): DeploymentStatusEntity {
            return DeploymentStatusEntity(
                    response.applicationId(),
                    response.environmentId(),
                    response.deploymentStrategyId(),
                    response.configurationProfileId(),
                    response.deploymentNumber() ?: 0,
                    response.description() ?: "",
                    response.stateAsString() ?: "",
                    if (response.hasEventLog()) response.eventLog().map { convert(it) } else listOf(),
                    (response.percentageComplete() ?: 0.0) as Float,
                    response.startedAt(),
                    response.completedAt())
        }

        private fun convert(it: DeploymentEvent) =
                DeploymentEventEntity(it.eventTypeAsString(), it.triggeredByAsString(), it.description(), it.occurredAt())

        fun convert(response: CreateConfigurationProfileResponse): ConfigurationProfileEntity {
            return ConfigurationProfileEntity(
                    response.id(),
                    response.applicationId(),
                    response.name(),
                    response.description(),
                    response.locationUri(),
                    response.retrievalRoleArn())
        }

        fun convert(response: ConfigurationProfileSummary): ConfigurationProfileEntity {
            return ConfigurationProfileEntity(
                    response.id(),
                    response.applicationId(),
                    response.name(),
                    "",
                    response.locationUri(),
                    "")
        }
    }
}