package com.siyamand.aws.dynamodb.core.repositories

import com.siyamand.aws.dynamodb.core.entities.CredentialEntity
import com.siyamand.aws.dynamodb.core.entities.TokenCredentialEntity

interface AWSBaseRepository<out T> {
    fun withToken(token: CredentialEntity): T
    fun withRegion(region: String): T
}