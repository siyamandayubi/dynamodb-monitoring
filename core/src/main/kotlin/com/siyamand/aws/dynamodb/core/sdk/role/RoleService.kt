package com.siyamand.aws.dynamodb.core.sdk.role

import com.siyamand.aws.dynamodb.core.sdk.authentication.CredentialEntity

interface RoleService {
    suspend fun getRoles(): List<RoleEntity>
    suspend fun getOrCreateLambdaRole(credentialEntity: CredentialEntity?): RoleEntity
    suspend fun getLambdaRole(): RoleEntity?
    suspend fun getOrCreateRdsProxyRole(credentialEntity: CredentialEntity?): RoleEntity
    suspend fun getOrCreateAppConfigRole(credentialEntity: CredentialEntity?): RoleEntity
    suspend fun getAppConfigRole(): RoleEntity?
    suspend fun threadSafe()
}