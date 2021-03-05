package com.siyamand.aws.dynamodb.core.sdk.role

import com.siyamand.aws.dynamodb.core.sdk.authentication.CredentialEntity
import com.siyamand.aws.dynamodb.core.common.NotExistException
import com.siyamand.aws.dynamodb.core.sdk.resource.ResourceRepository
import com.siyamand.aws.dynamodb.core.sdk.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.common.MonitorConfigProvider

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

    private suspend fun initialize(credentialEntity: CredentialEntity? = null) {

        val credential = credentialEntity ?: credentialProvider.getCredential()
        ?: throw SecurityException("No Credential has been provided")

        roleRepository.initialize(credential, credentialProvider.getGlobalRegion());
        resourceRepository.initialize(credential, credentialProvider.getRegion())
    }

    override suspend fun getOrCreateLambdaRole(credentialEntity: CredentialEntity?): RoleEntity {
        initialize(credentialEntity)

        val createRoleRequest = roleBuilder.createLambdaRole();
        val role = try {
            roleRepository.getRole(createRoleRequest.roleName)
        } catch (exp: NotExistException) {
            roleRepository.addRole(createRoleRequest)
        }

        val rolePolicies = roleRepository.getRolePolicies(role.name)

        addPolicyToRole(policyBuilder.createLambdaEc2Policy(), rolePolicies, role.name)
        addPolicyToRole(policyBuilder.createLambdaPolicy(), rolePolicies, role.name)
        addPolicyToRole(policyBuilder.createLambdaSecretManagerPolicy(), rolePolicies, role.name)
        addPolicyToRole(policyBuilder.createAccessRdsProxyPolicy(), rolePolicies, role.name)
        addPolicyToRole(policyBuilder.createAppConfigAccessPolicy(), rolePolicies, role.name)
        addPolicyToRole(policyBuilder.createS3AccessPolicy(), rolePolicies, role.name)

        return role
    }

    override suspend fun getOrCreateAppConfigRole(credentialEntity: CredentialEntity?): RoleEntity {
        initialize(credentialEntity)

        val createRoleRequest = roleBuilder.createAppConfigRole();
        val role = try {
            roleRepository.getRole(createRoleRequest.roleName)
        } catch (exp: NotExistException) {
            roleRepository.addRole(createRoleRequest)
        }

        val rolePolicies = roleRepository.getRolePolicies(role.name)

        addPolicyToRole(policyBuilder.createS3AccessPolicy(), rolePolicies, role.name)

        return role
    }
    override suspend fun getOrCreateRdsProxyRole(credentialEntity: CredentialEntity?): RoleEntity {
        initialize(credentialEntity)

        val createRoleRequest = roleBuilder.createRdsProxyRole();
        val role = try {
            roleRepository.getRole(createRoleRequest.roleName)
        } catch (exp: NotExistException) {
            roleRepository.addRole(createRoleRequest)
        }

        val rolePolicies = roleRepository.getRolePolicies(role.name)

        addPolicyToRole(policyBuilder.createRdsProxyPolicy(), rolePolicies, role.name)

        return role
    }

    override suspend fun getLambdaRole(): RoleEntity? {
        initialize()

        return try {
            roleRepository.getRole(monitorConfigProvider.getLambdaRole())
        } catch (exp: NotExistException) {
            null
        }
    }

     override suspend fun getAppConfigRole(): RoleEntity? {
        initialize()

        return try {
            roleRepository.getRole(monitorConfigProvider.getAppConfigRole())
        } catch (exp: NotExistException) {
            null
        }
    }

    private suspend fun addPolicyToRole(createPolicyRequest: CreatePolicyEntity, rolePolicies: List<String>, roleName: String) {
        if (!rolePolicies.any { it == createPolicyRequest.policyName }) {
            val policies = roleRepository.getPolicies(createPolicyRequest.path)
            val policy = if (policies.any()) policies.first() else roleRepository.addPolicy(createPolicyRequest)
            roleRepository.attachRolePolicy(roleName, policy.arn)
        }
    }
}