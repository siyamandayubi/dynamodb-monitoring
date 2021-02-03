package com.siyamand.aws.dynamodb.core.common

import com.siyamand.aws.dynamodb.core.authentication.CredentialEntity

interface AWSBaseRepository {
    fun initialize(token: CredentialEntity, region: String)
}