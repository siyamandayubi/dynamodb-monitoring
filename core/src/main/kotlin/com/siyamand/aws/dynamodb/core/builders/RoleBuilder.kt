package com.siyamand.aws.dynamodb.core.builders

import com.siyamand.aws.dynamodb.core.entities.CreateRoleEntity

interface RoleBuilder {
    fun createLambdaRole(): CreateRoleEntity
}