package com.siyamand.aws.dynamodb.core.sdk.appconfig

import com.siyamand.aws.dynamodb.core.sdk.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.sdk.role.RoleService
import com.siyamand.aws.dynamodb.core.sdk.s3.S3Service
import com.siyamand.aws.dynamodb.core.workflow.WorkflowInstance
import com.siyamand.aws.dynamodb.core.workflow.WorkflowResult
import com.siyamand.aws.dynamodb.core.workflow.WorkflowStep

class CreateAppConfigWorkflowStep(
        private var credentialProvider: CredentialProvider,
        private val appConfigRepository: AppConfigRepository,
        private val s3Service: S3Service,
        private val roleService: RoleService,
        private val appConfigBuilder: AppConfigBuilder) : WorkflowStep() {
    override val name: String = "CreateAppConfigWorkflow"

    override suspend fun execute(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {
        TODO("Not yet implemented")
    }

    override suspend fun isWaiting(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {
        TODO("Not yet implemented")
    }

    override suspend fun initialize() {
        this.credentialProvider = credentialProvider.threadSafe()
        this.s3Service.threadSafe()
    }
}