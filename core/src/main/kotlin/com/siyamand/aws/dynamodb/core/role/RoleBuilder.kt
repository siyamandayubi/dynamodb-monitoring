package com.siyamand.aws.dynamodb.core.role

interface RoleBuilder {
    val lambdaRoleName: String
    fun createLambdaRole(): CreateRoleEntity
}