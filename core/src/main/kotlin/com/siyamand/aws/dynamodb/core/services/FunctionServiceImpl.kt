package com.siyamand.aws.dynamodb.core.services

import com.siyamand.aws.dynamodb.core.entities.FunctionEntity
import com.siyamand.aws.dynamodb.core.repositories.LambdaRepository

class FunctionServiceImpl(private val lambdaRepository: LambdaRepository, private val credentialProvider: CredentialProvider) : FunctionService {
    override suspend fun getFunctions(): List<FunctionEntity> {
        val credential = credentialProvider.getCredential()
                ?: throw SecurityException("No Credential has been provided");

        lambdaRepository.initialize(credential, credentialProvider.getRegion());
        return lambdaRepository.getList()
    }
}