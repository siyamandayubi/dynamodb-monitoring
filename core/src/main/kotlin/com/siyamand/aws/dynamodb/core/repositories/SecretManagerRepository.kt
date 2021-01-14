package com.siyamand.aws.dynamodb.core.repositories

import com.siyamand.aws.dynamodb.core.entities.CreateSecretEntity
import com.siyamand.aws.dynamodb.core.entities.ResourceEntity

interface SecretManagerRepository : AWSBaseRepository {
    fun addSecret(entity: CreateSecretEntity): ResourceEntity
}