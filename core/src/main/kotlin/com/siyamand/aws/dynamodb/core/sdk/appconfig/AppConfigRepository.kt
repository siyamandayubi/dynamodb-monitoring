package com.siyamand.aws.dynamodb.core.sdk.appconfig

import com.siyamand.aws.dynamodb.core.common.AWSBaseRepository
import com.siyamand.aws.dynamodb.core.sdk.appconfig.entities.*

interface AppConfigRepository : AWSBaseRepository {
    suspend fun addApplication(entity: CreateApplicationEntity): ApplicationEntity
    suspend fun addEnvironment(entity: CreateEnvironmentEntity): EnvironmentEntity
    suspend fun addDeploymentStrategy(entity: CreateDeploymentStrategyEntity): DeploymentStrategyEntity
    suspend fun addConfigurationProfile(entity: CreateConfigurationProfileEntity): ConfigurationProfileEntity
    suspend fun addHostedConfigurationVersion(entity: CreateHostedConfigurationVersionEntity): String?
}