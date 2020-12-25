package com.siyamand.aws.dynamodb.core.services

import com.siyamand.aws.dynamodb.core.entities.TokenCredentialEntity

interface AuthenticationService {
    suspend fun getToken(accessKey: String, secretKey: String): TokenCredentialEntity
}