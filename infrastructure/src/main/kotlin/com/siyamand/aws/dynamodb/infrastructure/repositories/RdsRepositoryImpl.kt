package com.siyamand.aws.dynamodb.infrastructure.repositories

import com.siyamand.aws.dynamodb.core.rds.RdsEntity
import com.siyamand.aws.dynamodb.core.rds.RdsListEntity
import com.siyamand.aws.dynamodb.core.resource.ResourceEntity
import com.siyamand.aws.dynamodb.core.rds.CreateDbInstanceEntity
import com.siyamand.aws.dynamodb.core.rds.CreateDbProxyTargetEntity
import com.siyamand.aws.dynamodb.core.rds.CreateProxyEntity
import com.siyamand.aws.dynamodb.core.rds.DbProxyTargetEntity
import com.siyamand.aws.dynamodb.core.rds.RdsRepository
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilder
import com.siyamand.aws.dynamodb.infrastructure.mappers.RdsMapper
import com.siyamand.aws.dynamodb.infrastructure.mappers.ResourceMapper
import kotlinx.coroutines.reactive.awaitFirst
import reactor.core.publisher.Mono
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