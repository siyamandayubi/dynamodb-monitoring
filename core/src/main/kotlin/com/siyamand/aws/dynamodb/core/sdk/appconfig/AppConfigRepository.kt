package com.siyamand.aws.dynamodb.core.sdk.appconfig

import com.siyamand.aws.dynamodb.core.common.AWSBaseRepository
import com.siyamand.aws.dynamodb.core.common.PageResultEntity
import com.siyamand.aws.dynamodb.core.sdk.appconfig.entities.*

interface AppConfigRepository : AWSBaseRepository {
    suspend fun addApplication(entity: CreateApplicationEntity): ApplicationEntity
    suspend fun addEnvironment(entity: CreateEnvironmentEntity): EnvironmentEntity
    suspend fun addDeploymentStrategy(entity: CreateDeploymentStrategyEntity): DeploymentStrategyEntity
    suspend fun addConfigurationProfile(entity: CreateConfigurationProfileEntity): ConfigurationProfileEntity
    suspend fun addHostedConfigurationVersion(entity: CreateHostedConfigurationVersionEntity): HostedConfigurationVersionEntity
    suspend fun startDeployment(entity: StartDeploymentEntity): DeploymentStatusEntity
    suspend fun getDeployment(applicationId: String, environmentId: String, deploymentNumber: Int): DeploymentStatusEntity
    suspend fun getApplications(nextToken: String): PageResultEntity<ApplicationEntity>
    suspend fun getEnvironments(applicationId: String, nextToken: String): PageResultEntity<EnvironmentEntity>
    suspend fun getProfiles(applicationId: String, nextToken: String): PageResultEntity<ConfigurationProfileEntity>
    suspend fun getDeploymentStrategies(nextToken: String): PageResultEntity<DeploymentStrategyEntity>
    suspend fun getDeployments(applicationId: String, environmentId: String): PageResultEntity<DeploymentStatusEntity>
    suspend fun getHostedConfigurations(applicationId: String, profileId: String, nextToken: String): PageResultEntity<HostedConfigurationVersionEntity>
}