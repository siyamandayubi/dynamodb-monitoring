package com.siyamand.aws.dynamodb.core.authentication

interface AuthenticationService {
    suspend fun getToken(accessKey: String, secretKey: String): TokenCredentialEntity
}