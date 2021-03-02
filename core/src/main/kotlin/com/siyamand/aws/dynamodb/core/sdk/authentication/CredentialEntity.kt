package com.siyamand.aws.dynamodb.core.sdk.authentication

import java.util.*

interface CredentialEntity {
    var expiredIn: Date?
    val type: CredentialType
    var accessKey: String
    var secretKey: String
}