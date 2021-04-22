package com.siyamand.aws.dynamodb.core.sdk.authentication

import java.time.ZonedDateTime

interface CredentialEntity {
    var expiredIn: ZonedDateTime?
    val type: CredentialType
    var accessKey: String
    var secretKey: String
}