package com.siyamand.aws.dynamodb.infrastructure.repositories

import com.siyamand.aws.dynamodb.core.entities.CredentialEntity
import com.siyamand.aws.dynamodb.core.repositories.AWSBaseRepository
import com.siyamand.aws.dynamodb.core.repositories.TableRepository

open class AwsBaseRepositoryImpl : AWSBaseRepository {
    protected var token: CredentialEntity? = null
    protected var region: String = "us-east-2";


    override fun initialize(token: CredentialEntity, region: String)
    {
        this.token = token
        this.region = region
    }

}