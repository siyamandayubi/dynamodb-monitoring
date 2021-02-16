package com.siyamand.aws.dynamodb.core.lambda

import com.siyamand.aws.dynamodb.core.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.common.MonitorConfigProvider
import com.siyamand.aws.dynamodb.core.workflow.WorkflowContext
import com.siyamand.aws.dynamodb.core.workflow.WorkflowInstance
import com.siyamand.aws.dynamodb.core.workflow.WorkflowResult
import com.siyamand.aws.dynamodb.core.workflow.WorkflowStep

class AddLambdaFunctionWorkflowStep(private var credentialProvider: CredentialProvider,
                                    private val lambdaRepository: LambdaRepository,
                                    private val functionBuilder: FunctionBuilder,
                                    private val monitorConfigProvider: MonitorConfigProvider) : WorkflowStep() {
    override val name: String = "AddLambdaFunction"

    override suspend fun execute(instance: WorkflowInstance, context: WorkflowContext, params: Map<String, String>): WorkflowResult {
        TODO("Not yet implemented")
    }

    override suspend fun isWaiting(instance: WorkflowInstance, context: WorkflowContext, params: Map<String, String>): WorkflowResult {
        TODO("Not yet implemented")
    }

    override suspend fun initialize() {
       this.credentialProvider = credentialProvider.threadSafe()
    }
}