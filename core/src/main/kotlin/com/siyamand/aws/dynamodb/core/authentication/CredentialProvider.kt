package com.siyamand.aws.dynamodb.core.authentication

interface CredentialProvider {
    suspend fun getCredential(): CredentialEntity?
    fun getRegion(): String
    fun getGlobalRegion(): String
}