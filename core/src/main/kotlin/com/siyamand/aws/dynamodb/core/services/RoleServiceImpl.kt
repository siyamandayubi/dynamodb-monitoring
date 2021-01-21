package com.siyamand.aws.dynamodb.core.services

import com.siyamand.aws.dynamodb.core.builders.PolicyBuilder
import com.siyamand.aws.dynamodb.core.builders.RoleBuilder
import com.siyamand.aws.dynamodb.core.entities.CreatePolicyEntity
import com.siyamand.aws.dynamodb.core.entities.ResourceEntity
import com.siyamand.aws.dynamodb.core.entities.ResourceType
import com.siyamand.aws.dynamodb.core.entities.RoleEntity
import com.siyamand.aws.dynamodb.core.exceptions.NotExistException
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

    override suspend fun getOrCreateLambdaRole(): RoleEntity {
        initialize()

        val createRoleRequest = roleBuilder.createLambdaRole();
        val role = try {
            roleRepository.getRole(createRoleRequest.roleName)
        } catch (exp: NotExistException) {
            roleRepository.addRole(createRoleRequest)
        }

        val rolePolicies = roleRepository.getRolePolicies(role.name)

        addPolicyToRole(policyBuilder.createLambdaPolicy(), rolePolicies, role.name)
        addPolicyToRole(policyBuilder.createRdsProxyPolicy(), rolePolicies, role.name)

        return role
    }

    private suspend fun addPolicyToRole(createPolicyRequest: CreatePolicyEntity, rolePolicies: List<String>, roleName: String) {
        if (!rolePolicies.any { it == createPolicyRequest.policyName }) {
            val policies = roleRepository.getPolicies(createPolicyRequest.path)
            val policy = if (policies.any()) policies.first() else roleRepository.addPolicy(createPolicyRequest)
            roleRepository.attachRolePolicy(roleName, policy.arn)
        }
    }
}