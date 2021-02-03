package com.siyamand.aws.dynamodb.core.secretManager

import com.siyamand.aws.dynamodb.core.resource.ResourceEntity
import com.siyamand.aws.dynamodb.core.common.AWSBaseRepository

interface SecretManagerRepository : AWSBaseRepository {
    fun addSecret(entity: CreateSecretEntity): ResourceEntity
    fun getSecret(secretId: String): SecretEntity?
    fun deleteSecret(secretId: String)
}