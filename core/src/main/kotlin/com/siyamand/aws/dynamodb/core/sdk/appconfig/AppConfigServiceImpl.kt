package com.siyamand.aws.dynamodb.core.sdk.appconfig

import com.siyamand.aws.dynamodb.core.common.initializeRepositories
import com.siyamand.aws.dynamodb.core.sdk.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.sdk.role.RoleService
import com.siyamand.aws.dynamodb.core.sdk.s3.S3Service
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.charset.StandardCharsets

class AppConfigServiceImpl(
        private val appConfigRepository: AppConfigRepository,
        private val credentialProvider: CredentialProvider,
        private val s3Service: S3Service,
        private val roleService: RoleService,
        private val appConfigBuilder: AppConfigBuilder) : AppConfigService {

    override suspend fun createConfig(
            applicationName: String,
            environmentName: String,
            deploymentStrategyName: String,
            profileName: String,
            content: Map<String, String>) {
        val contentStr = Json.encodeToString(content)
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
                .getEnvironments(application.id, "")
                .items
                .firstOrNull { it.name == environmentName }
        if (environment == null) {
            val request = appConfigBuilder.build(application.id, environmentName, monitoringId)
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
                .getProfiles(application.id, "")
                .items
                .firstOrNull { it.name == profileName }
        val objectName = "$applicationName-config"
        if (profile == null) {
            val role = roleService.getAppConfigRole()
            val configS3 = s3Service.addObject(objectName, monitoringId, "json", contentStr.toByteArray(StandardCharsets.UTF_8))
            val request = appConfigBuilder.buildProfile(application.id, profileName, "s3://${configS3.bucket}/${configS3.key}", role!!.resource.arn, monitoringId)
            profile = appConfigRepository.addConfigurationProfile(request)
        }

        val objectVersion = s3Service.getObjectVersions(objectName).sortedByDescending { it.versionId }.first()

        val deployment = appConfigRepository
                .getDeployments(application.id, environment.id)
                .items
                .firstOrNull()
        if (deployment == null || deployment.state == "ROLLED_BACK") {
            val request = appConfigBuilder.buildDeployment(application.id,
                    environment.id,
                    deploymentStrategy.id,
                    profile.id,
                    objectVersion.versionId,
                    monitoringId)
            appConfigRepository.startDeployment(request)
        }
    }
}