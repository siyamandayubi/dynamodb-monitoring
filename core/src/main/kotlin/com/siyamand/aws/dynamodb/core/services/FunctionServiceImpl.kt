package com.siyamand.aws.dynamodb.core.services

import com.siyamand.aws.dynamodb.core.builders.FunctionBuilder
import com.siyamand.aws.dynamodb.core.entities.FunctionDetailEntity
import com.siyamand.aws.dynamodb.core.entities.FunctionEntity
import com.siyamand.aws.dynamodb.core.entities.ResourceEntity
import com.siyamand.aws.dynamodb.core.repositories.LambdaRepository

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