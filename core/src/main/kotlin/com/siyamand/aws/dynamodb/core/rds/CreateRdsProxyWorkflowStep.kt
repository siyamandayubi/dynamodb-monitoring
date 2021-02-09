package com.siyamand.aws.dynamodb.core.rds

import com.siyamand.aws.dynamodb.core.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.resource.ResourceRepository
import com.siyamand.aws.dynamodb.core.role.RoleService
import com.siyamand.aws.dynamodb.core.workflow.WorkflowContext
import com.siyamand.aws.dynamodb.core.workflow.WorkflowResult
import com.siyamand.aws.dynamodb.core.workflow.WorkflowStep

class CreateRdsProxyWorkflowStep(private val roleService: RoleService,
                                 private val credentialProvider: CredentialProvider,
                                 private val rdsRepository: RdsRepository,
                                 private val resourceRepository: ResourceRepository): WorkflowStep() {
    override val name: String
        get() = "CreateRdsProxy"

    override suspend fun execute(context: WorkflowContext, params: Map<String, String>): WorkflowResult {
        TODO("Not yet implemented")
    }

    override suspend fun isWaiting(context: WorkflowContext, params: Map<String, String>): WorkflowResult {
        TODO("Not yet implemented")
    }
}