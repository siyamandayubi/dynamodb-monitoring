package com.siyamand.aws.dynamodb.core.lambda

import com.siyamand.aws.dynamodb.core.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.common.initializeRepositories
import com.siyamand.aws.dynamodb.core.common.initializeRepositoriesWithGlobalRegion
import com.siyamand.aws.dynamodb.core.database.DatabaseCredentialEntity
import com.siyamand.aws.dynamodb.core.rds.RdsRepository
import com.siyamand.aws.dynamodb.core.role.RoleRepository
import com.siyamand.aws.dynamodb.core.secretManager.SecretManagerRepository
import com.siyamand.aws.dynamodb.core.workflow.*
import kotlinx.serialization.json.Json

class AddLambdaFunctionWorkflowStep(private var credentialProvider: CredentialProvider,
                                    private val lambdaRepository: LambdaRepository,
                                    private val roleRepository: RoleRepository,
                                    private val rdsRepository: RdsRepository,
                                    private val secretManagerRepository: SecretManagerRepository,
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

        if (!context.sharedData.containsKey(Keys.RDS_ARN_KEY)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "no ${Keys.RDS_ARN_KEY} in shared data")
        }

        if (!context.sharedData.containsKey(Keys.SECRET_ARN_KEY)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "no ${Keys.SECRET_ARN_KEY} in shared data")
        }

        if (!context.sharedData.containsKey(Keys.DATABASE_NAME)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "no ${Keys.DATABASE_NAME} in shared data")
        }

        credentialProvider.initializeRepositories(lambdaRepository, rdsRepository, secretManagerRepository)
        credentialProvider.initializeRepositoriesWithGlobalRegion(roleRepository)

        val rds = rdsRepository.getRds(context.sharedData[Keys.RDS_ARN_KEY]!!).firstOrNull()
                ?: return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "no Rds has been found for the given ARN: ${context.sharedData[Keys.RDS_ARN_KEY]} ")

        val secret = secretManagerRepository.getSecretValue(context.sharedData[Keys.SECRET_ARN_KEY]!!)
                ?: return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "no Secret has been found for the given ARN: ${context.sharedData[Keys.SECRET_ARN_KEY]} ")
        val credential = Json.decodeFromString(DatabaseCredentialEntity.serializer(), secret!!.secretData)

        val environmentVariables = mutableMapOf<String, String>()
        environmentVariables["sql_endpoint"] = rds.endPoint
        environmentVariables["sql_port"] = rds.port.toString()
        environmentVariables["username"] = credential.userName
        environmentVariables["password"] = credential.password
        environmentVariables["sql_database"] = context.sharedData[Keys.DATABASE_NAME]!!


        val name = (params["name"])!!
        val roleName = (params[Keys.LAMBDA_ROLE])!!
        val layersStr = (params["layers"])!!
        val layers = layersStr
                .split(",")
                .map { if (context.sharedData.containsKey(it)) (context.sharedData[it])!! else "" }
                .filter { !it.isNullOrEmpty() }

        val code = (context.sharedData[Keys.CODE_RESULT])!!
        val role = roleRepository.getRole(roleName)
        val createFunctionEntity = functionBuilder.build(name, code, role.resource.arn, layers, environmentVariables)
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