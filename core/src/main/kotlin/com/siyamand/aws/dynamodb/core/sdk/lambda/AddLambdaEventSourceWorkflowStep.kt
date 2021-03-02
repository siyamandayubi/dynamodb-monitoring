package com.siyamand.aws.dynamodb.core.sdk.lambda

import com.siyamand.aws.dynamodb.core.sdk.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.workflow.*

class AddLambdaEventSourceWorkflowStep(private var credentialProvider: CredentialProvider,
                                       private val functionBuilder: FunctionBuilder,
                                       private val lambdaRepository: LambdaRepository) : WorkflowStep() {
    override val name: String = "AddLambdaEventSource"

    override suspend fun execute(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {
        val context = instance.context
        if (!context.sharedData.containsKey(Keys.LAMBDA_ARN)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "no ${Keys.LAMBDA_ARN} in shared data")
        }

        if (!context.sharedData.containsKey(Keys.STREAM__DYNAMODB_ARN)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "no ${Keys.STREAM__DYNAMODB_ARN} in shared data")
        }

        val request = functionBuilder.buildEventSourceCreateRequest(context.sharedData[Keys.STREAM__DYNAMODB_ARN]!!, context.sharedData[Keys.LAMBDA_ARN]!!)
        val result = lambdaRepository.add(request)
        context.sharedData[Keys.EVENT_SOURCE_ARN] = result.arn
        return WorkflowResult(WorkflowResultType.SUCCESS, mapOf(Keys.EVENT_SOURCE_ARN to result.arn), "")
    }

    override suspend fun isWaiting(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {
        return execute(instance, owner, params)
    }

    override suspend fun initialize() {
        this.credentialProvider = credentialProvider.threadSafe()
    }
}