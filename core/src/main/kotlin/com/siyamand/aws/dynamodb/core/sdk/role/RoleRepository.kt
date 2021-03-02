package com.siyamand.aws.dynamodb.core.sdk.role

import com.siyamand.aws.dynamodb.core.sdk.resource.ResourceEntity
import com.siyamand.aws.dynamodb.core.common.AWSBaseRepository
import org.springframework.stereotype.Component

@Component
interface RoleRepository: AWSBaseRepository {
    suspend fun getRoles(): List<RoleEntity>
    suspend fun addRole(createRoleEntity: CreateRoleEntity): RoleEntity
    suspend fun addPolicy(createPolicyEntity: CreatePolicyEntity): ResourceEntity
    suspend fun attachRolePolicy(roleName: String, policyArn: String): Boolean
    suspend fun getPolicies(path: String): List<ResourceEntity>
    suspend fun getRolePolicies(roleName: String): List<String>
    suspend fun getRole(roleName: String): RoleEntity

}