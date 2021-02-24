package com.siyamand.aws.dynamodb.core.database
import kotlinx.serialization.Serializable

@Serializable
data class DatabaseCredentialEntity(val username: String, val password: String)