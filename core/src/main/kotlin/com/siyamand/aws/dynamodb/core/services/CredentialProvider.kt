package com.siyamand.aws.dynamodb.core.services

import com.siyamand.aws.dynamodb.core.entities.CredentialEntity

interface CredentialProvider {
    suspend fun getCredential(): CredentialEntity?
    fun getRegion(): String
}