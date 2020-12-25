package com.siyamand.aws.dynamodb.infrastructure.repositories

import kotlinx.coroutines.reactive.awaitFirst
import com.siyamand.aws.dynamodb.core.entities.CredentialEntity
import com.siyamand.aws.dynamodb.core.entities.FunctionEntity
import com.siyamand.aws.dynamodb.core.repositories.LambdaRepository
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilder
import com.siyamand.aws.dynamodb.infrastructure.mappers.CredentialMapper
import com.siyamand.aws.dynamodb.infrastructure.mappers.FunctionMapper
import reactor.core.publisher.Mono


class LambdaRepositoryImpl(private val clientBuilder: ClientBuilder) : LambdaRepository, AwsBaseRepositoryImpl() {
    override suspend fun getList(): List<FunctionEntity> {
        val credential = CredentialMapper.convert(this.token!!)

        val awsLambda = clientBuilder.buildAsyncAwsLambda(this.region, credential)
        val response = awsLambda.listFunctions().thenApply { it.functions().map(FunctionMapper::convert)  }
        return Mono.fromFuture(response).awaitFirst()
    }
 }