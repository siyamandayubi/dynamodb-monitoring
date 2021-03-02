package com.siyamand.aws.dynamodb.core.sdk.role

interface RoleBuilder {
    fun createLambdaRole(): CreateRoleEntity
    fun createRdsProxyRole(): CreateRoleEntity
}