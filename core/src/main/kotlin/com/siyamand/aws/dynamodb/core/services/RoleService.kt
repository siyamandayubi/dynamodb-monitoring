package com.siyamand.aws.dynamodb.core.services

import com.siyamand.aws.dynamodb.core.entities.ResourceEntity
import com.siyamand.aws.dynamodb.core.entities.RoleEntity

interface RoleService {
    suspend fun getRoles(): List<RoleEntity>
    suspend fun getOrCreateLambdaRole(): RoleEntity
}