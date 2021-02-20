package com.siyamand.aws.dynamodb.core.lambda

import com.siyamand.aws.dynamodb.core.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.workflow.*

class AddLambdaEventSourceWorkflowStep(private var credentialProvider: CredentialProvider,
                                       private val lambdaRepository: LambdaRepository) : WorkflowStep() {
    override val name: String = "AddLAmbdaEventSource"

    override suspend fun execute(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {
        val context = instance.context
        if (!context.sharedData.containsKey(Keys.LAMBDA_ARN)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "no ${Keys.LAMBDA_ARN} in shared data")
        }

        TODO("Not yet implemented")
    }

    override suspend fun isWaiting(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {
        return execute(instance, owner, params)
    }

    override suspend fun initialize() {
        this.credentialProvider = credentialProvider.threadSafe()
    }
}