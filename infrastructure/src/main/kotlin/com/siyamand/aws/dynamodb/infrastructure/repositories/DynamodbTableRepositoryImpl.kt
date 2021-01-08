package com.siyamand.aws.dynamodb.infrastructure.repositories

import kotlinx.coroutines.reactive.awaitFirst
import com.siyamand.aws.dynamodb.core.entities.CredentialEntity
import com.siyamand.aws.dynamodb.core.entities.TableDetailEntity
import com.siyamand.aws.dynamodb.core.entities.TableEntity
import com.siyamand.aws.dynamodb.core.repositories.TableRepository
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilder
import com.siyamand.aws.dynamodb.infrastructure.mappers.CredentialMapper
import com.siyamand.aws.dynamodb.infrastructure.mappers.TableMapper
import reactor.core.publisher.Mono.*
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.model.*
import software.amazon.awssdk.services.resourcegroupstaggingapi.model.GetResourcesRequest
import software.amazon.awssdk.services.resourcegroupstaggingapi.model.TagFilter


class DynamodbTableRepositoryImpl(private val clientBuilder: ClientBuilder) : TableRepository, AwsBaseRepositoryImpl() {

    override suspend fun getDetail(tableName: String): TableDetailEntity? {
        val db = getClient(clientBuilder::buildAsyncDynamodb)
        val response = db.describeTable(DescribeTableRequest.builder().tableName(tableName).build())

        val returnValue = response.thenApply(TableMapper::convertDetail)
        return fromFuture(returnValue).awaitFirst()
    }

    override suspend fun getList(): List<TableEntity> {
        val db = getClient(clientBuilder::buildAsyncDynamodb)
        val response = db.listTables().thenApply { tablesResponse -> tablesResponse.tableNames().map { name -> TableEntity(name, null) } }

        return fromFuture(response).awaitFirst()
    }

    override suspend fun add(t: TableDetailEntity) {
        val db = getClient(clientBuilder::buildAsyncDynamodb)
        val createTableRequest = CreateTableRequest
                .builder()
                .tableName(t.tableName)
                .attributeDefinitions(t.attributes.map { TableMapper.convertToAttributeDefinition(it.attributeName, it.attributeType) })
                .keySchema(t.keySchema.map { TableMapper.convertToKeySchemaDefinition(it.attributeName, it.keyType) })
                .build()
        val response = db.createTable(createTableRequest)

        val mono = fromFuture(response)
        mono.awaitFirst()
    }

    override suspend fun enableStream(tableName: String): TableDetailEntity?{
        val db = getClient(clientBuilder::buildAsyncDynamodb)
        val updateTableRequest = UpdateTableRequest
                .builder()
                .tableName(tableName)
                .streamSpecification(StreamSpecification
                        .builder()
                        .streamEnabled(true)
                        .streamViewType(StreamViewType.NEW_AND_OLD_IMAGES)
                        .build())
                .build()
        val returnValue = db.updateTable(updateTableRequest).thenApply { TableMapper.convertDetail(it.tableDescription()) }

        return fromFuture(returnValue).awaitFirst()
    }
}