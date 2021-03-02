package com.siyamand.aws.dynamodb.core.common

import com.siyamand.aws.dynamodb.core.sdk.authentication.CredentialEntity

interface AWSBaseRepository {
    fun initialize(token: CredentialEntity, region: String)
}