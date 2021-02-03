package com.siyamand.aws.dynamodb.core.database
import kotlinx.serialization.Serializable

@Serializable
data class DatabaseCredentialEntity(val userName: String, val password: String)