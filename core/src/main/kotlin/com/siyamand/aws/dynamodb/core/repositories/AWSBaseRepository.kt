package com.siyamand.aws.dynamodb.core.repositories

import com.siyamand.aws.dynamodb.core.entities.CredentialEntity
import com.siyamand.aws.dynamodb.core.entities.TokenCredentialEntity

interface AWSBaseRepository {
    fun initialize(token: CredentialEntity,region: String)
}