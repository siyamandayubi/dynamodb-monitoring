package com.siyamand.aws.dynamodb.core.lambda

import com.siyamand.aws.dynamodb.core.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.common.MonitorConfigProvider
import com.siyamand.aws.dynamodb.core.common.initializeRepositories
import com.siyamand.aws.dynamodb.core.workflow.*

class AddLambdaLayerWorkflowStep(
        private var credentialProvider: CredentialProvider,
        private val lambdaRepository: LambdaRepository,
        private val functionBuilder: FunctionBuilder,
        private val monitorConfigProvider: MonitorConfigProvider) : WorkflowStep() {
    override val name: String = "AddLambdaLayer"

    override suspend fun execute(context: WorkflowContext, params: Map<String, String>): WorkflowResult {

        if (!params.containsKey(Keys.LAMBDA_LAYER_PATH) ||
                !params.containsKey(Keys.LAMBDA_LAYER_NAME)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "The required keys ${Keys.LAMBDA_LAYER_NAME}, ${Keys.LAMBDA_LAYER_PATH} not found in params")
        }

        var forceCreation = params.containsKey(Keys.FORCE_CREATE) && params[Keys.FORCE_CREATE] == "true"

        credentialProvider.initializeRepositories(lambdaRepository)

        val existingVersions = lambdaRepository.getLayer(params[Keys.LAMBDA_LAYER_NAME]!!)
        if (!forceCreation && existingVersions.items.any()) {
            var layer = existingVersions.items.maxByOrNull { it.version }!!
            context.sharedData[Keys.LAMBDA_LAYER_ARN_KEY] = layer.layerVersionEntity.arn
            return WorkflowResult(WorkflowResultType.SUCCESS, mapOf(Keys.LAMBDA_LAYER_ARN_KEY to layer.layerVersionEntity.arn), "")
        }

        val description = if (params.containsKey("description")) params["description"]!! else ""
        var layer = lambdaRepository.add(functionBuilder.buildLayer(params[Keys.LAMBDA_LAYER_NAME]!!, description, params[Keys.LAMBDA_LAYER_PATH]!!))
        context.sharedData[Keys.LAMBDA_LAYER_ARN_KEY] = layer.layerVersionEntity.arn
        return WorkflowResult(WorkflowResultType.SUCCESS, mapOf(Keys.LAMBDA_LAYER_ARN_KEY to layer.layerVersionEntity.arn), "")
    }

    override suspend fun isWaiting(context: WorkflowContext, params: Map<String, String>): WorkflowResult {
        return execute(context, params)
    }

    override suspend fun initialize() {
        this.credentialProvider = credentialProvider.threadSafe()
    }
}