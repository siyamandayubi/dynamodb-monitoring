package com.siyamand.aws.dynamodb.core.sdk.lambda

import com.siyamand.aws.dynamodb.core.sdk.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.common.initializeRepositories
import com.siyamand.aws.dynamodb.core.common.initializeRepositoriesWithGlobalRegion
import com.siyamand.aws.dynamodb.core.sdk.network.VpcRepository
import com.siyamand.aws.dynamodb.core.sdk.rds.RdsRepository
import com.siyamand.aws.dynamodb.core.sdk.role.RoleRepository
import com.siyamand.aws.dynamodb.core.sdk.secretManager.SecretManagerRepository
import com.siyamand.aws.dynamodb.core.workflow.*

class AddLambdaFunctionWorkflowStep(private var credentialProvider: CredentialProvider,
                                    private val lambdaRepository: LambdaRepository,
                                    private val roleRepository: RoleRepository,
                                    private val rdsRepository: RdsRepository,
                                    private val secretManagerRepository: SecretManagerRepository,
                                    private val vpcRepository: VpcRepository,
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

        if (!context.sharedData.containsKey(Keys.PROXY_NAME)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "no ${Keys.PROXY_NAME} in shared data")
        }

        if (!context.sharedData.containsKey(Keys.SECRET_ARN_KEY)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "no ${Keys.SECRET_ARN_KEY} in shared data")
        }

        if (!context.sharedData.containsKey(Keys.DATABASE_NAME)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "no ${Keys.DATABASE_NAME} in shared data")
        }

        credentialProvider.initializeRepositories(lambdaRepository, rdsRepository, secretManagerRepository)
        credentialProvider.initializeRepositoriesWithGlobalRegion(roleRepository)

        val vpcList = vpcRepository.getVpcs(true, listOf())
        if (!vpcList.items.any()) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "no default VPC has been found")
        }
        val vpc = vpcList.items.first()
        val subnetIds = vpcRepository.getSubnets(listOf(vpc.vpcId))
        val securityGroups = vpcRepository.getSecurityGroupsByVpcs(listOf(vpc.vpcId))


        val rds = rdsRepository.getProxies(context.sharedData[Keys.PROXY_NAME]!!).items.firstOrNull()
                ?: return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "no Rds has been found for the given ARN: ${context.sharedData[Keys.RDS_ARN_KEY]} ")

        if (secretManagerRepository.getSecretValue(context.sharedData[Keys.SECRET_ARN_KEY]!!) == null) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "no Secret has been found for the given ARN: ${context.sharedData[Keys.SECRET_ARN_KEY]} ")
        }

        val environmentVariables = mutableMapOf<String, String>()
        environmentVariables["sql_endpoint"] = rds.endpoint ?: ""
        environmentVariables["sql_port"] = "3306"
        environmentVariables["secret_key"] = context.sharedData[Keys.SECRET_ARN_KEY]!!
        environmentVariables["sql_database"] = context.sharedData[Keys.DATABASE_NAME]!!

        val originalName = (params["name"])!!
        var name = originalName
        var counter = 0;
        while (lambdaRepository.getDetail(name) != null) {
            counter++;
            name = "$originalName-$counter"
        }

        val roleName = (params[Keys.LAMBDA_ROLE])!!
        val layersStr = (params["layers"])!!
        val layers = layersStr
                .split(",")
                .map { if (context.sharedData.containsKey(it)) (context.sharedData[it])!! else "" }
                .filter { !it.isNullOrEmpty() }

        val code = (context.sharedData[Keys.CODE_RESULT])!!
        val role = roleRepository.getRole(roleName)
        val createFunctionEntity = functionBuilder.build(name,
                code,
                role.resource.arn,
                layers,
                environmentVariables,
                subnetIds,
                securityGroups)
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