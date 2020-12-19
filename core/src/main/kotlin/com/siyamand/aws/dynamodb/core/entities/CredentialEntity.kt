package com.siyamand.aws.dynamodb.core.entities

import java.util.*

interface CredentialEntity {
    var expiredIn: Date?
    val type: CredentialType
    var accessKey: String
    var secretKey: String
}