package com.siyamand.aws.dynamodb.core.sdk.authentication

import java.time.ZonedDateTime

class BasicCredentialEntity(override var accessKey: String,override var secretKey: String, override var expiredIn: ZonedDateTime?): CredentialEntity {
    override val type: CredentialType = CredentialType.BASIC
}