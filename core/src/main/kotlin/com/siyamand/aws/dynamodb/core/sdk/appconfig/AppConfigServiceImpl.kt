package com.siyamand.aws.dynamodb.core.sdk.appconfig

import com.siyamand.aws.dynamodb.core.common.initializeRepositories
import com.siyamand.aws.dynamodb.core.sdk.authentication.CredentialProvider

class AppConfigServiceImpl(
        private val appConfigRepository: AppConfigRepository,
        private val credentialProvider: CredentialProvider,
        private val appConfigBuilder: AppConfigBuilder) : AppConfigService {

    override suspend fun createConfig(
            applicationName: String,
            environmentName: String,
            deploymentStrategyName: String,
            profileName: String,
            content: String) {
        credentialProvider.initializeRepositories(appConfigRepository)
        val monitoringId = "test"
        // application
        var application = appConfigRepository
                .getApplications("")
                .items
                .firstOrNull() { it.name == applicationName }
        if (application == null) {
            val request = appConfigBuilder.build(applicationName, monitoringId)
            application = appConfigRepository.addApplication(request)
        }

        //environment
        var environment = appConfigRepository
                .getEnvironments(application.id!!, "")
                .items
                .firstOrNull { it.name == environmentName }
        if (environment == null) {
            val request = appConfigBuilder.build(application.id!!, environmentName, monitoringId)
            environment = appConfigRepository.addEnvironment(request)
        }

        var deploymentStrategy = appConfigRepository
                .getDeploymentStrategies("")
                .items
                .firstOrNull { it.name == deploymentStrategyName }
        if (deploymentStrategy == null) {
            val request = appConfigBuilder.buildDeploymentStrategy(deploymentStrategyName, monitoringId)
            deploymentStrategy = appConfigRepository.addDeploymentStrategy(request)
        }

        var profile = appConfigRepository
                .getProfiles(application.id!!, "")
                .items
                .firstOrNull { it.name == profileName }
        if (profile == null) {
            val request = appConfigBuilder.buildProfile(application.id!!, "", profileName, monitoringId)
            profile = appConfigRepository.addConfigurationProfile(request)
        }

        var hostedConfig = appConfigRepository
                .getHostedConfigurations(application.id!!, "")
                .items
                .lastOrNull()
        if (hostedConfig == null) {
            val request = appConfigBuilder.buildHostedConfiguration(application.id!!, profile.id, content)
            hostedConfig = appConfigRepository.addHostedConfigurationVersion(request)
        }

        var deployment = appConfigRepository
                .getDeployments(application.id!!, environment.id!!)
                .items
                .firstOrNull()
        if (deployment == null) {
            val request = appConfigBuilder.buildDeployment(application.id!!,
                    environment!!.id!!,
                    deploymentStrategy!!.id!!,
                    profile.id,
                    hostedConfig.latestVersionNumber.toString(),
                    monitoringId)
            deployment = appConfigRepository.startDeployment(request)
        }
        TODO()
    }
}