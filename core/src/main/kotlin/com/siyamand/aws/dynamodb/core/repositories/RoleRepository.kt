package com.siyamand.aws.dynamodb.core.repositories

import com.siyamand.aws.dynamodb.core.entities.CreatePolicyEntity
import com.siyamand.aws.dynamodb.core.entities.CreateRoleEntity
import com.siyamand.aws.dynamodb.core.entities.ResourceEntity
import com.siyamand.aws.dynamodb.core.entities.RoleEntity
import org.springframework.stereotype.Component

@Component
interface RoleRepository: AWSBaseRepository {
    suspend fun getList(): List<RoleEntity>
    suspend fun addRole(createRoleEntity: CreateRoleEntity): ResourceEntity
    suspend fun addPolicy(createPolicyEntity: CreatePolicyEntity): ResourceEntity
    suspend fun attachRolePolicy(roleName: String, policyArn: String): Boolean

}