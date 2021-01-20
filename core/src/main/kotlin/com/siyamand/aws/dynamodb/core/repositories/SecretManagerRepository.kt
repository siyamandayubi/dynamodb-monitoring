package com.siyamand.aws.dynamodb.core.repositories

import com.siyamand.aws.dynamodb.core.entities.CreateSecretEntity
import com.siyamand.aws.dynamodb.core.entities.ResourceEntity
import com.siyamand.aws.dynamodb.core.entities.SecretEntity

interface SecretManagerRepository : AWSBaseRepository {
    fun addSecret(entity: CreateSecretEntity): ResourceEntity
    fun getSecret(secretId: String): SecretEntity?
    fun deleteSecret(secretId: String)
}