package com.siyamand.aws.dynamodb.core.sdk.authentication

interface TokenRepository {
    suspend fun getAccessToken(keyId: String, secretAcessId: String): TokenCredentialEntity
    fun withRegion(region: String): TokenRepository
}