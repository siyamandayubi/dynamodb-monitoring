package com.siyamand.aws.dynamodb.core.role

interface RoleBuilder {
    fun createLambdaRole(): CreateRoleEntity
}