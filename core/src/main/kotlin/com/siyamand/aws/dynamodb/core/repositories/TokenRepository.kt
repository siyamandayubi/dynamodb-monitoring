package com.siyamand.aws.dynamodb.core.repositories

import com.siyamand.aws.dynamodb.core.entities.TokenCredentialEntity

interface TokenRepository {
    suspend fun getAccessToken(keyId: String, secretAcessId: String): TokenCredentialEntity
    fun withRegion(region: String): TokenRepository
}