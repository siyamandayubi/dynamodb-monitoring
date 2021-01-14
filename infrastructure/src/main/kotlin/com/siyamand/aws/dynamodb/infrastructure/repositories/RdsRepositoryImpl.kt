package com.siyamand.aws.dynamodb.infrastructure.repositories

import com.siyamand.aws.dynamodb.core.entities.RdsEntity
import com.siyamand.aws.dynamodb.core.entities.ResourceEntity
import com.siyamand.aws.dynamodb.core.entities.database.CreateDbInstanceEntity
import com.siyamand.aws.dynamodb.core.entities.database.CreateProxyEntity
import com.siyamand.aws.dynamodb.core.repositories.RdsRepository
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilder
import com.siyamand.aws.dynamodb.infrastructure.mappers.CredentialMapper
import com.siyamand.aws.dynamodb.infrastructure.mappers.RdsMapper
import com.siyamand.aws.dynamodb.infrastructure.mappers.ResourceMapper
import kotlinx.coroutines.reactive.awaitFirst
import reactor.core.publisher.Mono
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.rds.RdsAsyncClient
import software.amazon.awssdk.services.rds.model.CreateDbInstanceRequest
import software.amazon.awssdk.services.rds.model.CreateDbProxyRequest

class RdsRepositoryImpl(private val clientBuilder: ClientBuilder) : RdsRepository, AwsBaseRepositoryImpl() {
    override suspend fun getRds(name: String): RdsEntity {
        val client = getClient(clientBuilder::buildAsyncRdsClient)
        TODO("Not yet implemented")
    }

    override suspend fun createProxy(entity: CreateProxyEntity): ResourceEntity {
        val client = getClient(clientBuilder::buildAsyncRdsClient)
        val request = RdsMapper.convert(entity)
        val response = client.createDBProxy(request).thenApply { ResourceMapper.convert(it.dbProxy().dbProxyArn()) }

        return Mono.fromFuture(response).awaitFirst()
    }

    override suspend fun createDatabase(entity: CreateDbInstanceEntity): ResourceEntity {
        val client = getClient(clientBuilder::buildAsyncRdsClient)
        val request = RdsMapper.convert(entity)

        val response = client.createDBInstance( request).thenApply { ResourceMapper.convert(it.dbInstance().dbInstanceArn()) }
        return Mono.fromFuture(response).awaitFirst()
    }

}