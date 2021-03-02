package com.siyamand.aws.dynamodb.core.sdk.secretManager

import com.siyamand.aws.dynamodb.core.sdk.resource.ResourceEntity
import com.siyamand.aws.dynamodb.core.common.AWSBaseRepository

interface SecretManagerRepository : AWSBaseRepository {
    fun addSecret(entity: CreateSecretEntity): ResourceEntity
    fun getSecretValue(secretId: String): SecretEntity?
    fun deleteSecret(secretId: String)
    fun getSecretByArn(arn: String): SecretEntity?
    fun getSecretDetail(secretId: String): SecretDetailEntity?
}