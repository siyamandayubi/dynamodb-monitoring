package com.siyamand.aws.dynamodb.infrastructure.repositories

import com.siyamand.aws.dynamodb.core.common.PageResultEntity
import com.siyamand.aws.dynamodb.core.sdk.rds.*
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilder
import com.siyamand.aws.dynamodb.infrastructure.mappers.RdsMapper
import kotlinx.coroutines.reactive.awaitFirst
import reactor.core.publisher.Mono
import software.amazon.awssdk.services.rds.model.*

class RdsRepositoryImpl(private val clientBuilder: ClientBuilder) : RdsRepository, AwsBaseRepositoryImpl() {
    override suspend fun getRds(identifier: String): List<RdsEntity> {
        val client = getClient(clientBuilder::buildAsyncRdsClient)
        val response = client.describeDBInstances(DescribeDbInstancesRequest
                .builder()
                .filters(Filter.builder().name("db-instance-id").values(identifier).build())
                .build())
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

    override suspend fun createProxy(entity: CreateProxyEntity): RdsProxyEntity {
        val client = getClient(clientBuilder::buildAsyncRdsClient)
        val request = RdsMapper.convert(entity)
        val response = client.createDBProxy(request).thenApply { RdsMapper.convert(it.dbProxy()) }
        val x = DBProxyTarget.builder().build()
        return Mono.fromFuture(response).awaitFirst()
    }

    override suspend fun getProxies(name: String): PageResultEntity<RdsProxyEntity> {
        val client = getClient(clientBuilder::buildAsyncRdsClient)
        val response = client
                .describeDBProxies(DescribeDbProxiesRequest.builder().dbProxyName(name).build())
                .thenApply {
                    PageResultEntity<RdsProxyEntity>(it.dbProxies().map(RdsMapper::convert), it.marker() ?: "")
                }

        return Mono.fromFuture(response).awaitFirst()
    }

    override suspend fun getDbProxyTargets(targetGroupName: String, dbProxyName: String): PageResultEntity<DbProxyTargetEntity> {
        val client = getClient(clientBuilder::buildAsyncRdsClient)
        val response = client.describeDBProxyTargets(DescribeDbProxyTargetsRequest
                .builder()
                .targetGroupName(targetGroupName)
                .dbProxyName(dbProxyName)
                .build()).thenApply { PageResultEntity(it.targets().map(RdsMapper::convert), it.marker() ?: "") }

        return Mono.fromFuture(response).awaitFirst()
    }

    override suspend fun registerDbProxyTarget(entity: CreateDbProxyTargetEntity): List<DbProxyTargetEntity> {
        val client = getClient(clientBuilder::buildAsyncRdsClient)
        val request = RdsMapper.convert(entity)
        val response = client.registerDBProxyTargets(request).thenApply { it.dbProxyTargets().map(RdsMapper::convert) }
        return Mono.fromFuture(response).awaitFirst()
    }

    override suspend fun getDbProxyTargetGroups(dbProxyName: String): PageResultEntity<DbProxyTargetGroupEntity> {
        val client = getClient(clientBuilder::buildAsyncRdsClient)
        val response = client
                .describeDBProxyTargetGroups(DescribeDbProxyTargetGroupsRequest.builder().dbProxyName(dbProxyName).build())
                .thenApply {
                    PageResultEntity<DbProxyTargetGroupEntity>(it.targetGroups().map(RdsMapper::convert), it.marker()
                            ?: "")
                }
        return Mono.fromFuture(response).awaitFirst()
    }

    override suspend fun createRds(entity: CreateDbInstanceEntity): RdsEntity {
        val client = getClient(clientBuilder::buildAsyncRdsClient)
        val request = RdsMapper.convert(entity)

        val response = client.createDBInstance(request).thenApply { RdsMapper.convert(it.dbInstance()) }
        return Mono.fromFuture(response).awaitFirst()
    }

}