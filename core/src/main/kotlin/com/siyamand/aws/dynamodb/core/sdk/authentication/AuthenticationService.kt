package com.siyamand.aws.dynamodb.core.sdk.authentication

interface AuthenticationService {
    suspend fun getToken(accessKey: String, secretKey: String): TokenCredentialEntity
}