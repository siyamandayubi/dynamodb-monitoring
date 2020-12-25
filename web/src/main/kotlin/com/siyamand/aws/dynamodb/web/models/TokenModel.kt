package com.siyamand.aws.dynamodb.web.models

import java.util.*

class TokenModel(public val token:String,public val expiredIn: Date?) {
}