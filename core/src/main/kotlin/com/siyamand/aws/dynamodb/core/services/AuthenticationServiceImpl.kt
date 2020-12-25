package com.siyamand.aws.dynamodb.core.services

import com.siyamand.aws.dynamodb.core.entities.TokenCredentialEntity
import com.siyamand.aws.dynamodb.core.repositories.TokenRepository

class AuthenticationServiceImpl(private  val tokenRepository: TokenRepository): AuthenticationService {
    override suspend fun getToken(accessKey: String, secretKey: String): TokenCredentialEntity {
        return tokenRepository.getAccessToken(accessKey, secretKey)
    }
}