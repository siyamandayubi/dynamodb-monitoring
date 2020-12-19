package com.siyamand.aws.dynamodb.core.services

import com.siyamand.aws.dynamodb.core.entities.RoleEntity
import com.siyamand.aws.dynamodb.core.repositories.RoleRepository

class RoleServiceImpl(
        private val roleRepository: RoleRepository,
        private val credentialProvider: CredentialProvider) : RoleService {

    override suspend fun getRoles(): List<RoleEntity> {
        val credential = credentialProvider.getCredential()
                ?: throw SecurityException("No Credential has been provided");

        roleRepository.withToken(credential);
        return roleRepository.getList()
    }
}