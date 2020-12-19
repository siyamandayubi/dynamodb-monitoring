package com.siyamand.aws.dynamodb.core.entities

import java.util.*

class TokenCredentialEntity(override var accessKey: String,override var secretKey: String, var sessionToken: String, override var expiredIn: Date?) : CredentialEntity {
    override val type: CredentialType = CredentialType.TOKEN
}