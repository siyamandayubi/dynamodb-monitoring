package com.siyamand.aws.dynamodb.infrastructure.repositories

import com.siyamand.aws.dynamodb.core.entities.RdsEntity
import com.siyamand.aws.dynamodb.core.repositories.RdsRepository
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilder
import com.siyamand.aws.dynamodb.infrastructure.mappers.CredentialMapper
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.rds.RdsAsyncClient

class RdsRepositoryImpl(private val clientBuilder: ClientBuilder) : RdsRepository, AwsBaseRepositoryImpl() {
    override suspend fun getRds(name: String): RdsEntity {
        val client = getClient()
        TODO("Not yet implemented")
    }
    private fun getClient(): RdsAsyncClient {
        if (this.token == null) {
            throw IllegalArgumentException("token is not provider")
        }

        if (this.region.isNullOrEmpty()) {
            throw java.lang.IllegalArgumentException("region is not provider")
        }

        val credential = CredentialMapper.convert(this.token!!)
        return clientBuilder.buildAsyncRdsClient(region, credential)
    }
}