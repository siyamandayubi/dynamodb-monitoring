package com.siyamand.aws.dynamodb.core.authentication

class AuthenticationServiceImpl(private  val tokenRepository: TokenRepository): AuthenticationService {
    override suspend fun getToken(accessKey: String, secretKey: String): TokenCredentialEntity {
        return tokenRepository.getAccessToken(accessKey, secretKey)
    }
}