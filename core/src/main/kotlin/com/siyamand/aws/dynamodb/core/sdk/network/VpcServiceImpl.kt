package com.siyamand.aws.dynamodb.core.sdk.network

import com.siyamand.aws.dynamodb.core.sdk.authentication.CredentialProvider

class VpcServiceImpl(
        private val credentialProvider: CredentialProvider,
        private val vpcRepository: VpcRepository) : VpcService {

    override suspend fun getEndpoints(): List<EndpointEntity> {
        initialize()
        return vpcRepository.getEndpoints("", null).items
    }

    override suspend fun getRegions(): List<RegionEntity> {
        initialize()
        return vpcRepository.getRegions()
    }

    private suspend fun initialize() {
        val credential = credentialProvider.getCredential()
                ?: throw SecurityException("No Credential has been provided")

        vpcRepository.initialize(credential, credentialProvider.getRegion())
    }
}