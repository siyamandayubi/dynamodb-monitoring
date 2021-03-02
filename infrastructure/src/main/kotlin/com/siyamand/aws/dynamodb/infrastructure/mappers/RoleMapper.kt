package com.siyamand.aws.dynamodb.infrastructure.mappers

import com.siyamand.aws.dynamodb.core.sdk.role.CreatePolicyEntity
import com.siyamand.aws.dynamodb.core.sdk.role.CreateRoleEntity
import com.siyamand.aws.dynamodb.core.sdk.role.RoleEntity
import software.amazon.awssdk.services.iam.model.CreatePolicyRequest
import software.amazon.awssdk.services.iam.model.CreateRoleRequest
import software.amazon.awssdk.services.iam.model.Tag

class RoleMapper {
    companion object {

        fun convert(role: software.amazon.awssdk.services.iam.model.Role): RoleEntity {
            return RoleEntity(
                    role.roleName(),
                    ResourceMapper.convert(role.arn()),
                    role.description(),
                    role.path(),
                    role.assumeRolePolicyDocument())
        }

        fun convert(entity: CreateRoleEntity): CreateRoleRequest {
            val tags = entity.tags.map { Tag.builder().key(it.name).value(it.value).build()  }
            val builder = CreateRoleRequest.builder()
                    .roleName(entity.roleName)
                    .description(entity.description)
                    .tags(tags)
                    .maxSessionDuration(entity.maxSessionDuration)
                    .permissionsBoundary(entity.permissionsBoundary)

            if (!entity.assumeRolePolicyDocument.isNullOrEmpty()){
                builder.assumeRolePolicyDocument(entity.assumeRolePolicyDocument)
            }
            return builder.build()
        }

        fun convert(entity: CreatePolicyEntity): CreatePolicyRequest {
            return CreatePolicyRequest.builder()
                    .policyName(entity.policyName)
                    .path(entity.path)
                    .description(entity.description)
                    .policyDocument(entity.policyDocument)
                    .build()
        }
    }
}