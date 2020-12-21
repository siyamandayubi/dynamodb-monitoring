package com.siyamand.aws.dynamodb.infrastructure.repositories

import kotlinx.coroutines.reactive.awaitFirst
import com.siyamand.aws.dynamodb.core.entities.CredentialEntity
import com.siyamand.aws.dynamodb.core.entities.FunctionEntity
import com.siyamand.aws.dynamodb.core.repositories.LambdaRepository
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilder
import com.siyamand.aws.dynamodb.infrastructure.mappers.CredentialMapper
import com.siyamand.aws.dynamodb.infrastructure.mappers.FunctionMapper
import reactor.core.publisher.Mono


class LambdaRepositoryImpl(private val clientBuilder: ClientBuilder) : LambdaRepository {
    private var token: CredentialEntity? = null
    private var region: String = "us-east-2";
    override suspend fun getList(): List<FunctionEntity> {
        val credential = CredentialMapper.convert(this.token!!)

        val awsLambda = clientBuilder.buildAsyncAwsLambda(this.region, credential)
        val response = awsLambda.listFunctions().thenApply { it.functions().map(FunctionMapper::convert)  }
        return Mono.fromFuture(response).awaitFirst()
    }

    override suspend fun add(t: FunctionEntity) {
        TODO("Not yet implemented")
    }

    override fun edit(t: FunctionEntity) {
        TODO("Not yet implemented")
    }

    override fun delete(t: FunctionEntity) {
        TODO("Not yet implemented")
    }

    override fun withToken(token: CredentialEntity): LambdaRepository {
        this.token = token
        return this;
    }

    override fun withRegion(region: String): LambdaRepository {
        this.region = region
        return this;
    }
 }