package com.siyamand.aws.dynamodb.core.lambda

import com.siyamand.aws.dynamodb.core.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.common.initializeRepositories
import com.siyamand.aws.dynamodb.core.common.initializeRepositoriesWithGlobalRegion
import com.siyamand.aws.dynamodb.core.role.RoleRepository
import com.siyamand.aws.dynamodb.core.workflow.*

class AddLambdaFunctionWorkflowStep(private var credentialProvider: CredentialProvider,
                                    private val lambdaRepository: LambdaRepository,
                                    private val roleRepository: RoleRepository,
                                    private val functionBuilder: FunctionBuilder) : WorkflowStep() {
    override val name: String = "AddLambdaFunction"

    override suspend fun execute(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {

        val context = instance.context
        if (!params.containsKey("name")) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "name parameter is mandatory")
        }

        if (!params.containsKey(Keys.LAMBDA_ROLE)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "role parameter is mandatory")
        }

        if (!params.containsKey("layers")) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "layers parameter is mandatory")
        }

        if (!context.sharedData.containsKey(Keys.CODE_RESULT)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "code parameter is mandatory")
        }

        credentialProvider.initializeRepositories(lambdaRepository)
        credentialProvider.initializeRepositoriesWithGlobalRegion(roleRepository)

        val name = (params["name"])!!
        val roleName = (params[Keys.LAMBDA_ROLE])!!
        val layersStr = (params["layers"])!!
        val layers = layersStr
                .split(",")
                .map { if(context.sharedData.containsKey(it)) (context.sharedData[it])!! else "" }
                .filter { !it.isNullOrEmpty() }

        val code = (context.sharedData[Keys.CODE_RESULT])!!
        val role = roleRepository.getRole(roleName)
        val createFunctionEntity = functionBuilder.build(name, code, role.resource.arn, layers)
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