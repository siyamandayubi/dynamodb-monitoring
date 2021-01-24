package com.siyamand.aws.dynamodb.infrastructure.repositories

import com.siyamand.aws.dynamodb.core.entities.RdsEntity
import com.siyamand.aws.dynamodb.core.entities.RdsListEntity
import com.siyamand.aws.dynamodb.core.entities.ResourceEntity
import com.siyamand.aws.dynamodb.core.entities.database.CreateDbInstanceEntity
import com.siyamand.aws.dynamodb.core.entities.database.CreateDbProxyTargetEntity
import com.siyamand.aws.dynamodb.core.entities.database.CreateProxyEntity
import com.siyamand.aws.dynamodb.core.entities.database.DbProxyTargetEntity
import com.siyamand.aws.dynamodb.core.repositories.RdsRepository
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilder
import com.siyamand.aws.dynamodb.infrastructure.mappers.CredentialMapper
import com.siyamand.aws.dynamodb.infrastructure.mappers.RdsMapper
import com.siyamand.aws.dynamodb.infrastructure.mappers.ResourceMapper
import kotlinx.coroutines.reactive.awaitFirst
import reactor.core.publisher.Mono
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.rds.RdsAsyncClient
import software.amazon.awssdk.services.rds.model.*

class RdsRepositoryImpl(private val clientBuilder: ClientBuilder) : RdsRepository, AwsBaseRepositoryImpl() {
    override suspend fun getRds(name: String): List<RdsEntity> {
        val client = getClient(clientBuilder::buildAsyncRdsClient)
        val response = client.describeDBInstances(DescribeDbInstancesRequest.builder().dbInstanceIdentifier(name).build())
                .thenApply { it.dbInstances().map(RdsMapper::convert) }

        return Mono.fromFuture(response).awaitFirst()
    }

    override suspend fun list(marker: String): RdsListEntity {
        val client = getClient(clientBuilder::buildAsyncRdsClient)
        val request = DescribeDbInstancesRequest.builder()
        if (!marker.isNullOrEmpty()) {
            request.marker(marker)
        }
        val response = client.describeDBInstances(request.build())
                .thenApply { RdsListEntity(it.marker(), it.dbInstances().map(RdsMapper::convert)) }

        return Mono.fromFuture(response).awaitFirst()
    }

    override suspend fun createProxy(entity: CreateProxyEntity): ResourceEntity {
        val client = getClient(clientBuilder::buildAsyncRdsClient)
        val request = RdsMapper.convert(entity)
        val response = client.createDBProxy(request).thenApply { ResourceMapper.convert(it.dbProxy().dbProxyArn()) }
        val x = DBProxyTarget.builder().build()
        return Mono.fromFuture(response).awaitFirst()
    }

    override suspend fun registerDbProxyTarget(entity: CreateDbProxyTargetEntity): List<DbProxyTargetEntity> {
        val client = getClient(clientBuilder::buildAsyncRdsClient)
        val request = RdsMapper.convert(entity)
        val response = client.registerDBProxyTargets(request).thenApply { it.dbProxyTargets().map(RdsMapper::convert) }
        return Mono.fromFuture(response).awaitFirst()
    }

    override suspend fun createDatabase(entity: CreateDbInstanceEntity): ResourceEntity {
        val client = getClient(clientBuilder::buildAsyncRdsClient)
        val request = RdsMapper.convert(entity)

        val response = client.createDBInstance(request).thenApply { ResourceMapper.convert(it.dbInstance().dbInstanceArn()) }
        return Mono.fromFuture(response).awaitFirst()
    }

}