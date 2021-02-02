package com.siyamand.aws.dynamodb.core.services

import com.siyamand.aws.dynamodb.core.entities.network.EndpointEntity
import com.siyamand.aws.dynamodb.core.repositories.VpcRepository

class VpcServiceImpl(
        private val credentialProvider: CredentialProvider,
        private val vpcRepository: VpcRepository) : VpcService {

    override suspend fun getEndpoints(): List<EndpointEntity> {
        initialize()
        return vpcRepository.getEndpoints("",null).items
    }

    private suspend fun initialize() {
        val credential = credentialProvider.getCredential()
                ?: throw SecurityException("No Credential has been provided");

        vpcRepository.initialize(credential, credentialProvider.getRegion());
    }
}