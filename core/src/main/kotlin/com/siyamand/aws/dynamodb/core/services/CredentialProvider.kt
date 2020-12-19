package com.siyamand.aws.dynamodb.core.services

import com.siyamand.aws.dynamodb.core.entities.CredentialEntity

interface CredentialProvider {
    fun getCredential(): CredentialEntity?
}