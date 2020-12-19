package com.siyamand.aws.dynamodb.core.entities

import java.util.*

class BasicCredentialEntity(override var accessKey: String,override var secretKey: String, override var expiredIn: Date?): CredentialEntity {
    override val type: CredentialType = CredentialType.BASIC
}