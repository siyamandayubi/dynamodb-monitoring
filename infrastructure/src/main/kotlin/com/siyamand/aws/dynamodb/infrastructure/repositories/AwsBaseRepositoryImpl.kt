package com.siyamand.aws.dynamodb.infrastructure.repositories

import com.siyamand.aws.dynamodb.core.sdk.authentication.CredentialEntity
import com.siyamand.aws.dynamodb.core.common.AWSBaseRepository
import com.siyamand.aws.dynamodb.infrastructure.mappers.CredentialMapper
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider

open class AwsBaseRepositoryImpl : AWSBaseRepository {
    protected var token: CredentialEntity? = null
    protected var region: String = "us-east-2";


    override fun initialize(token: CredentialEntity, region: String)
    {
        this.token = token
        this.region = region
    }
    protected fun <T> getClient(builder: (region: String, credential: AwsCredentialsProvider)-> T): T {
        if (this.token == null) {
            throw IllegalArgumentException("token is not provider")
        }

        if (this.region.isNullOrEmpty()) {
            throw java.lang.IllegalArgumentException("region is not provider")
        }

        val credential = CredentialMapper.convert(this.token!!)
        return builder(region, credential)
    }
}