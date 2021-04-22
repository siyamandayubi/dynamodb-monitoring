package com.siyamand.aws.dynamodb.core.sdk.authentication

import java.time.ZonedDateTime

class TokenCredentialEntity(
        override var accessKey: String,
        override var secretKey: String,
        var sessionToken: String,
        override var expiredIn: ZonedDateTime?) : CredentialEntity {
    override val type: CredentialType = CredentialType.TOKEN
}