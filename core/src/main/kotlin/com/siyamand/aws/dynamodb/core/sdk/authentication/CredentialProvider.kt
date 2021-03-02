package com.siyamand.aws.dynamodb.core.sdk.authentication

interface CredentialProvider {
    suspend fun getCredential(): CredentialEntity?
    fun getRegion(): String
    fun getGlobalRegion(): String
    suspend fun threadSafe(): CredentialProvider
}