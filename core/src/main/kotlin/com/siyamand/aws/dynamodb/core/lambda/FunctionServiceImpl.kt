package com.siyamand.aws.dynamodb.core.lambda

import com.siyamand.aws.dynamodb.core.resource.ResourceEntity
import com.siyamand.aws.dynamodb.core.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.common.MonitorConfigProvider
import com.siyamand.aws.dynamodb.core.role.RoleService

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

    override suspend fun getDetail(name: String): FunctionDetailEntity {
        val credential = credentialProvider.getCredential()
                ?: throw SecurityException("No Credential has been provided");

        lambdaRepository.initialize(credential, credentialProvider.getRegion());
        return lambdaRepository.getDetail(name)
    }

    override suspend fun addLambda(functionName: String, code: String): ResourceEntity {
        initialize()
        val role = roleService.getOrCreateLambdaRole()
        val createRequest = functionBuilder.build(functionName, code, role.resource.arn)
        return lambdaRepository.add(createRequest)
    }

    private suspend fun initialize() {
        val credential = credentialProvider.getCredential()
                ?: throw SecurityException("No Credential has been provided");

        lambdaRepository.initialize(credential, credentialProvider.getRegion());
    }
}