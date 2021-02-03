package com.siyamand.aws.dynamodb.core.role

interface RoleService {
    suspend fun getRoles(): List<RoleEntity>
    suspend fun getOrCreateLambdaRole(): RoleEntity
}