package com.siyamand.aws.dynamodb.core.services

import com.siyamand.aws.dynamodb.core.builders.PolicyBuilder
import com.siyamand.aws.dynamodb.core.builders.RoleBuilder
import com.siyamand.aws.dynamodb.core.entities.ResourceEntity
import com.siyamand.aws.dynamodb.core.entities.RoleEntity
import com.siyamand.aws.dynamodb.core.repositories.ResourceRepository
import com.siyamand.aws.dynamodb.core.repositories.RoleRepository

class RoleServiceImpl(
        private val roleRepository: RoleRepository,
        private val resourceRepository: ResourceRepository,
        private val credentialProvider: CredentialProvider,
        private val monitorConfigProvider: MonitorConfigProvider,
        private val policyBuilder: PolicyBuilder,
        private val roleBuilder: RoleBuilder) : RoleService {

    override suspend fun getRoles(): List<RoleEntity> {
        initialize()
        return roleRepository.getRoles()
    }

    private suspend fun initialize() {
        val credential = credentialProvider.getCredential()
                ?: throw SecurityException("No Credential has been provided");

        roleRepository.initialize(credential, credentialProvider.getGlobalRegion());
        resourceRepository.initialize(credential, credentialProvider.getRegion())
    }

    override suspend fun createLambdaRole(): ResourceEntity {
        initialize()
        val resources = resourceRepository.getResources(monitorConfigProvider.getRoleTagName(), null, null, null)

        if (resources.items.isNotEmpty()) {
            return resources.items.first()
        }

        val createPolicyRequest = policyBuilder.createLambdaPolicy()
        val policies = roleRepository.getPolicies(createPolicyRequest.path)

        val policy = if (policies.any()) policies.first() else roleRepository.addPolicy(createPolicyRequest)
        val createRoleRequest = roleBuilder.createLambdaRole();
        val role = roleRepository.addRole(createRoleRequest)
        roleRepository.attachRolePolicy(createRoleRequest.roleName, policy.arn)
        return role
    }
}