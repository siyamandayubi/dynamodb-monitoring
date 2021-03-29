package com.siyamand.aws.dynamodb.core.sdk.lambda

import com.siyamand.aws.dynamodb.core.sdk.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.common.initializeRepositories
import com.siyamand.aws.dynamodb.core.monitoring.MonitoringResourcePersister
import com.siyamand.aws.dynamodb.core.workflow.*

class AddLambdaLayerWorkflowStep(
        private var credentialProvider: CredentialProvider,
        private val lambdaRepository: LambdaRepository,
        private val functionBuilder: FunctionBuilder,
        private val monitoringResourcePersister: MonitoringResourcePersister) : WorkflowStep() {
    override val name: String = "AddLambdaLayer"

    override suspend fun execute(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {

        val context = instance.context
        if (!params.containsKey(Keys.LAMBDA_LAYER_PATH) ||
                !params.containsKey(Keys.LAMBDA_LAYER_NAME)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "The required keys ${Keys.LAMBDA_LAYER_NAME}, ${Keys.LAMBDA_LAYER_PATH} not found in params")
        }

        val outputKey = if (params.containsKey("output")) (params["output"])!! else Keys.LAMBDA_LAYER_ARN_KEY

        val forceCreation = params.containsKey(Keys.FORCE_CREATE) && params[Keys.FORCE_CREATE] == "true"

        credentialProvider.initializeRepositories(lambdaRepository)

        val existingVersions = lambdaRepository.getLayer(params[Keys.LAMBDA_LAYER_NAME]!!)
        if (!forceCreation && existingVersions.items.any()) {
            val layer = existingVersions.items.maxByOrNull { it.version }!!
            context.sharedData[outputKey] = layer.layerVersionEntity.arn
            return WorkflowResult(WorkflowResultType.SUCCESS, mapOf(outputKey to layer.layerVersionEntity.arn), "")
        }

        val description = if (params.containsKey("description")) params["description"] else ""
        val layer = lambdaRepository.add(functionBuilder.buildLayer(params[Keys.LAMBDA_LAYER_NAME] ?: "", description
                ?: "", params[Keys.LAMBDA_LAYER_PATH] ?: ""))
        context.sharedData[outputKey] = layer.layerVersionEntity.arn
        monitoringResourcePersister.persist(instance.id, layer.layerVersionEntity.arn)
        return WorkflowResult(WorkflowResultType.SUCCESS, mapOf(outputKey to layer.layerVersionEntity.arn), "")
    }

    override suspend fun isWaiting(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {
        return execute(instance, owner, params)
    }

    override suspend fun initialize() {
        monitoringResourcePersister.threadSafe()
        this.credentialProvider = credentialProvider.threadSafe()
    }
}