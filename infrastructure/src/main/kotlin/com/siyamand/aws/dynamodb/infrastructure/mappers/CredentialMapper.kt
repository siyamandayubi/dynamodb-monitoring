package com.siyamand.aws.dynamodb.infrastructure.mappers

import com.siyamand.aws.dynamodb.core.entities.BasicCredentialEntity
import com.siyamand.aws.dynamodb.core.entities.CredentialEntity
import com.siyamand.aws.dynamodb.core.entities.CredentialType
import com.siyamand.aws.dynamodb.core.entities.TokenCredentialEntity
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider

class CredentialMapper {
    companion object {

        fun convert(entity: CredentialEntity): AwsCredentialsProvider {
            if (entity.type == CredentialType.TOKEN) {
                val entityToken: TokenCredentialEntity = entity as TokenCredentialEntity
                return StaticCredentialsProvider.create(AwsSessionCredentials.create(entityToken.accessKey, entityToken.secretKey, entityToken.sessionToken))
            } else if (entity.type == CredentialType.BASIC) {
                val entityBasic: BasicCredentialEntity = entity as BasicCredentialEntity
                return StaticCredentialsProvider.create(AwsBasicCredentials.create(entityBasic.accessKey, entityBasic.secretKey))
            } else {
                throw Exception("conversion from CredentialEntity to Credentials failed")
            }
        }
    }
}