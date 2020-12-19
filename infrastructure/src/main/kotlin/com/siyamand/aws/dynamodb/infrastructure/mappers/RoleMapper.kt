package com.siyamand.aws.dynamodb.infrastructure.mappers

import com.siyamand.aws.dynamodb.core.entities.RoleEntity

class RoleMapper {
    companion object {

        fun convert(role: software.amazon.awssdk.services.iam.model.Role): RoleEntity {
            return RoleEntity(role.roleName())
        }
    }
}