package com.siyamand.aws.dynamodb.infrastructure.mappers

import com.siyamand.aws.dynamodb.core.entities.CreatePolicyEntity
import com.siyamand.aws.dynamodb.core.entities.CreateRoleEntity
import com.siyamand.aws.dynamodb.core.entities.RoleEntity
import software.amazon.awssdk.services.iam.model.CreatePolicyRequest
import software.amazon.awssdk.services.iam.model.CreateRoleRequest

class RoleMapper {
    companion object {

        fun convert(role: software.amazon.awssdk.services.iam.model.Role): RoleEntity {
            return RoleEntity(role.roleName())
        }

        fun convert(entity: CreateRoleEntity): CreateRoleRequest {
            return CreateRoleRequest.builder()
                    .roleName(entity.roleName)
                    .description(entity.description)
                    .assumeRolePolicyDocument(entity.assumeRolePolicyDocument)
                    .maxSessionDuration(entity.maxSessionDuration)
                    .permissionsBoundary(entity.permissionsBoundary)
                    .build()
        }

        fun convert(entity: CreatePolicyEntity): CreatePolicyRequest {
            return CreatePolicyRequest.builder()
                    .policyName(entity.policyName)
                    .description(entity.description)
                    .policyDocument(entity.policyDocument)
                    .build()
        }
    }
}