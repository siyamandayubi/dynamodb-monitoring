package com.siyamand.aws.dynamodb.core.authentication

import java.util.*

interface CredentialEntity {
    var expiredIn: Date?
    val type: CredentialType
    var accessKey: String
    var secretKey: String
}