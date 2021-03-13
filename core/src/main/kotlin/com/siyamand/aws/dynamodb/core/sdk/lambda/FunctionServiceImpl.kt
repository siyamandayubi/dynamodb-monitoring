package com.siyamand.aws.dynamodb.core.sdk.lambda

import com.siyamand.aws.dynamodb.core.sdk.resource.ResourceEntity
import com.siyamand.aws.dynamodb.core.sdk.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.common.MonitorConfigProvider
import com.siyamand.aws.dynamodb.core.common.PageResultEntity
import com.siyamand.aws.dynamodb.core.common.initializeRepositories
import com.siyamand.aws.dynamodb.core.common.initializeRepositoriesWithGlobalRegion
import com.siyamand.aws.dynamodb.core.sdk.role.RoleService

class FunctionServiceImpl(
        private val monitorConfigProvider: MonitorConfigProvider,
        private val functionBuilder: FunctionBuilder,
        private val roleService: RoleService,
        private val lambdaRepository: LambdaRepository,
        private val credentialProvider: CredentialProvider) : FunctionService {
    override suspend fun getFunctions(): List<FunctionEntity> {
        initialize()
        return lambdaRepository.getList()
    }

    override suspend fun getDetail(name: String): FunctionDetailEntity? {
        val credential = credentialProvider.getCredential()
                ?: throw SecurityException("No Credential has been provided");

        lambdaRepository.initialize(credential, credentialProvider.getRegion());
        return lambdaRepository.getDetail(name)
    }

    override suspend fun getLayers(marker: String): PageResultEntity<FunctionLayerListEntity> {
        credentialProvider.initializeRepositoriesWithGlobalRegion(lambdaRepository)
        return lambdaRepository.getLayers(marker)
    }
    override suspend fun getLayer(name: String): PageResultEntity<FunctionLayerEntity> {
        credentialProvider.initializeRepositories(lambdaRepository)
        return lambdaRepository.getLayer(name)
    }
    override suspend fun addLambda(functionName: String, code: String): ResourceEntity {
        initialize()
        val role = roleService.getOrCreateLambdaRole(null)
        val createRequest = functionBuilder.build(functionName, code, role.resource.arn, listOf(), mapOf(), listOf(), listOf())
        return lambdaRepository.add(createRequest)
    }

    private suspend fun initialize() {
        val credential = credentialProvider.getCredential()
                ?: throw SecurityException("No Credential has been provided");

        lambdaRepository.initialize(credential, credentialProvider.getRegion());
    }
}