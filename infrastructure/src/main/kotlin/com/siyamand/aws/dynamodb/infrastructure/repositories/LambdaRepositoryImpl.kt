package com.siyamand.aws.dynamodb.infrastructure.repositories

import com.siyamand.aws.dynamodb.core.common.PageResultEntity
import com.siyamand.aws.dynamodb.core.lambda.*
import com.siyamand.aws.dynamodb.core.resource.ResourceEntity
import kotlinx.coroutines.reactive.awaitFirst
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilder
import com.siyamand.aws.dynamodb.infrastructure.mappers.FunctionMapper
import com.siyamand.aws.dynamodb.infrastructure.mappers.ResourceMapper
import reactor.core.publisher.Mono
import software.amazon.awssdk.services.lambda.model.GetFunctionRequest
import software.amazon.awssdk.services.lambda.model.ListLayerVersionsRequest
import software.amazon.awssdk.services.lambda.model.PublishLayerVersionRequest


class LambdaRepositoryImpl(private val clientBuilder: ClientBuilder) : LambdaRepository, AwsBaseRepositoryImpl() {
    override suspend fun getList(): List<FunctionEntity> {
        val awsLambda = getClient(clientBuilder::buildAsyncAwsLambda)
        val response = awsLambda.listFunctions().thenApply { it.functions().map(FunctionMapper::convert) }
        return Mono.fromFuture(response).awaitFirst()
    }

    override suspend fun getLayer(name: String): PageResultEntity<FunctionLayerEntity> {
        val awsLambda = getClient(clientBuilder::buildAsyncAwsLambda)
        val response = awsLambda
                .listLayerVersions(ListLayerVersionsRequest.builder().layerName(name).build())
                .thenApply { PageResultEntity(it.layerVersions().map(FunctionMapper::convert), it.nextMarker() ?: "") }

        return Mono.fromFuture(response).awaitFirst()
    }

    override suspend fun getDetail(name: String): FunctionDetailEntity {
        val client = getClient(clientBuilder::buildAsyncAwsLambda)
        val response = client.getFunction(GetFunctionRequest.builder().functionName(name).build()).thenApply(FunctionMapper::convert)
        return Mono.fromFuture(response).awaitFirst()
    }

    override suspend fun add(requestEntity: CreateFunctionRequestEntity): ResourceEntity {
        val request = FunctionMapper.convert(requestEntity)
        val awsLambda = getClient(clientBuilder::buildAsyncAwsLambda)
        val response = awsLambda.createFunction(request).thenApply { ResourceMapper.convert(it.functionArn()) }
        return Mono.fromFuture(response).awaitFirst()
    }

    override suspend fun add(requestEntity: CreateEventSourceRequestEntity): ResourceEntity {
        val request = FunctionMapper.convert(requestEntity)
        val awsLambda = getClient(clientBuilder::buildAsyncAwsLambda)
        val response = awsLambda.createEventSourceMapping(request).thenApply { ResourceMapper.convert(it.eventSourceArn()) }
        return Mono.fromFuture(response).awaitFirst()
    }

    override suspend fun add(entity: CreateLayerEntity): FunctionLayerEntity {
        val client = getClient(clientBuilder::buildAsyncAwsLambda)
        val request = FunctionMapper.convert(entity)
        val response = client.publishLayerVersion(request).thenApply { FunctionMapper.convert(it) }
        return Mono.fromFuture(response).awaitFirst()
    }
}