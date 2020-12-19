package com.siyamand.aws.dynamodb.infrastructure.repositories

import kotlinx.coroutines.reactive.awaitFirst
import com.siyamand.aws.dynamodb.core.entities.CredentialEntity
import com.siyamand.aws.dynamodb.core.entities.TableDetailEntity
import com.siyamand.aws.dynamodb.core.entities.TableEntity
import com.siyamand.aws.dynamodb.core.repositories.TableRepository
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilder
import com.siyamand.aws.dynamodb.infrastructure.mappers.CredentialMapper
import com.siyamand.aws.dynamodb.infrastructure.mappers.TableMapper
import reactor.core.publisher.Mono
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest


class TableRepositoryImpl(private val clientBuilder: ClientBuilder) : TableRepository {
    private var token: CredentialEntity? = null
    private var region: String = "us-east-2";

    override suspend fun getDetail(tableName: String): TableDetailEntity {
        val db = asyncDynamoDb()
        val response = db.describeTable(DescribeTableRequest.builder().tableName(tableName).build())
        val returnValue = response.thenApply(TableMapper::convertDetail)
        return Mono.fromFuture(returnValue).awaitFirst()
    }

    override suspend fun getList(): List<TableEntity> {
        val db = asyncDynamoDb()
        val response = db.listTables().thenApply { tablesResponse -> tablesResponse.tableNames().map { name -> TableEntity(name, null) } }

        return Mono.fromFuture(response).awaitFirst()
    }

    override fun add(t: TableEntity) {
        TODO("Not yet implemented")
    }

    override fun edit(t: TableEntity) {
        TODO("Not yet implemented")
    }

    override fun delete(t: TableEntity) {
        TODO("Not yet implemented")
    }


    private fun asyncDynamoDb(): DynamoDbAsyncClient {
        if (this.token == null) {
            throw IllegalArgumentException("token is not provider")
        }

        if (this.region.isNullOrEmpty()) {
            throw java.lang.IllegalArgumentException("region is not provider")
        }

        val credential = CredentialMapper.convert(this.token!!)
        return clientBuilder.buildAsyncDynamodb(region, credential)
    }

    override fun withToken(token: CredentialEntity): TableRepository {
        this.token = token
        return this
    }

    override fun withRegion(region: String): TableRepository {
        this.region = region
        return this
    }
}