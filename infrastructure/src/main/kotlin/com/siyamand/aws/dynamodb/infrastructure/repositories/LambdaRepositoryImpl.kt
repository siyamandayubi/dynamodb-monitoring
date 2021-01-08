package com.siyamand.aws.dynamodb.infrastructure.repositories

import com.siyamand.aws.dynamodb.core.entities.*
import kotlinx.coroutines.reactive.awaitFirst
import com.siyamand.aws.dynamodb.core.repositories.LambdaRepository
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilder
import com.siyamand.aws.dynamodb.infrastructure.mappers.CredentialMapper
import com.siyamand.aws.dynamodb.infrastructure.mappers.FunctionMapper
import com.siyamand.aws.dynamodb.infrastructure.mappers.ResourceMapper
import reactor.core.publisher.Mono
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.lambda.LambdaAsyncClient
import software.amazon.awssdk.services.lambda.model.CreateAliasRequest
import software.amazon.awssdk.services.lambda.model.CreateEventSourceMappingRequest
import software.amazon.awssdk.services.lambda.model.CreateFunctionRequest
import software.amazon.awssdk.services.lambda.model.GetFunctionRequest
import software.amazon.awssdk.services.s3.model.GetBucketLocationRequest


class LambdaRepositoryImpl(private val clientBuilder: ClientBuilder) : LambdaRepository, AwsBaseRepositoryImpl() {
    override suspend fun getList(): List<FunctionEntity> {
        val awsLambda = getClient(clientBuilder::buildAsyncAwsLambda)
        val response = awsLambda.listFunctions().thenApply { it.functions().map(FunctionMapper::convert) }
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
}