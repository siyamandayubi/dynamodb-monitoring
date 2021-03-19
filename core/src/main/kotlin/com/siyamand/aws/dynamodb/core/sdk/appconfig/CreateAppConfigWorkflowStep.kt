package com.siyamand.aws.dynamodb.core.sdk.appconfig

import com.siyamand.aws.dynamodb.core.common.initializeRepositories
import com.siyamand.aws.dynamodb.core.sdk.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.sdk.role.RoleService
import com.siyamand.aws.dynamodb.core.sdk.s3.S3Service
import com.siyamand.aws.dynamodb.core.workflow.*
import java.nio.charset.StandardCharsets

class CreateAppConfigWorkflowStep(
        private var credentialProvider: CredentialProvider,
        private val appConfigRepository: AppConfigRepository,
        private val s3Service: S3Service,
        private val roleService: RoleService,
        private val appConfigBuilder: AppConfigBuilder) : WorkflowStep() {
    override val name: String = "CreateAppConfig"

    override suspend fun execute(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {
        if (!params.containsKey("applicationName")) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "applicationName param is mandatory")
        }
        val applicationName = params["applicationName"]!!

        if (!params.containsKey("environmentName")) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "environmentName param is mandatory")
        }
        val environmentName = params["environmentName"]!!

        if (!params.containsKey("deploymentStrategyName")) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "deploymentStrategyName param is mandatory")
        }
        val deploymentStrategyName = params["deploymentStrategyName"]!!

        if (!params.containsKey("profileName")) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "profileName param is mandatory")
        }
        val profileName = params["profileName"]!!

        if (!params.containsKey("appConfigContent")) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "appConfigContent param is mandatory")
        }
        val appConfigKey = params["appConfigContent"]!!
        if (!instance.context.sharedData.containsKey(appConfigKey)){
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "appConfigContent param is mandatory")
        }
        val content = instance.context.sharedData[appConfigKey]!!
        val monitoringId = instance.id

        credentialProvider.initializeRepositories(appConfigRepository)
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
        val objectName = "$applicationName-config"
        if (profile == null) {
            val role = roleService.getAppConfigRole()
            val configS3 = s3Service.addObject(objectName, monitoringId, "json", content.toByteArray(StandardCharsets.UTF_8))
            val request = appConfigBuilder.buildProfile(application.id!!, profileName, "s3://${configS3.bucket}/${configS3.key}", role!!.resource.arn, monitoringId)
            profile = appConfigRepository.addConfigurationProfile(request)
        }

        val objectVersion = s3Service.getObjectVersions(objectName).sortedByDescending { it.versionId }.first()

        var deployment = appConfigRepository
                .getDeployments(application.id!!, environment.id!!)
                .items
                .sortedByDescending { it.deploymentNumber }
                .firstOrNull()
        if (deployment == null) {
            val request = appConfigBuilder.buildDeployment(application.id!!,
                    environment!!.id!!,
                    deploymentStrategy!!.id!!,
                    profile.id,
                    objectVersion.versionId,
                    monitoringId)
            deployment = appConfigRepository.startDeployment(request)
        }

        instance.context.sharedData[Keys.DEPLOYMNET_NUMBER] = deployment!!.deploymentNumber?.toString() ?: ""

        val workflowResult = if (deployment.state == "COMPLETE") {
            WorkflowResultType.SUCCESS
        } else if (deployment.state == "DEPLOYING" || deployment.state == "VALIDATING") {
            WorkflowResultType.WAITING
        } else {
            WorkflowResultType.ERROR
        }

        val message = if (workflowResult == WorkflowResultType.ERROR) "deployment failed" else ""
        return WorkflowResult(workflowResult, mapOf(), message)
    }

    override suspend fun isWaiting(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {
        return execute(instance, owner, params)
    }

    override suspend fun initialize() {
        this.credentialProvider = credentialProvider.threadSafe()
        this.s3Service.threadSafe()
    }
}