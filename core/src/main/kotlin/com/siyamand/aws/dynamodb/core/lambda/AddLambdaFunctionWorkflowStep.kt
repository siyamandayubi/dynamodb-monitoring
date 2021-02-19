package com.siyamand.aws.dynamodb.core.lambda

import com.siyamand.aws.dynamodb.core.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.common.MonitorConfigProvider
import com.siyamand.aws.dynamodb.core.workflow.*

class AddLambdaFunctionWorkflowStep(private var credentialProvider: CredentialProvider,
                                    private val lambdaRepository: LambdaRepository,
                                    private val functionBuilder: FunctionBuilder) : WorkflowStep() {
    override val name: String = "AddLambdaFunction"

    override suspend fun execute(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {

        if (!params.containsKey("name")) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "name parameter is mandatory")
        }

        if (!params.containsKey(Keys.LAMBDA_ROLE)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "role parameter is mandatory")
        }

        if (!params.containsKey("layers")) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "layers parameter is mandatory")
        }

        if (!instance.context.sharedData.containsKey(Keys.CODE_RESULT)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "code parameter is mandatory")
        }

        val name = (params["name"])!!
        val role = (params[Keys.LAMBDA_ROLE])!!
        val layersStr = (params["layers"])!!
        val layers = layersStr.split(",")
        val code = (instance.context.sharedData[Keys.CODE_RESULT])!!
        val createFunctionEntity = functionBuilder.build(name, code, role, layers)
        val result = lambdaRepository.add(createFunctionEntity)
        instance.context.sharedData[Keys.LAMBDA_ARN] = result.arn

        return WorkflowResult(WorkflowResultType.SUCCESS, mapOf(Keys.LAMBDA_ARN to result.arn), "")
    }

    override suspend fun isWaiting(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {
        return execute(instance, owner, params)
    }

    override suspend fun initialize() {
        this.credentialProvider = credentialProvider.threadSafe()
    }
}