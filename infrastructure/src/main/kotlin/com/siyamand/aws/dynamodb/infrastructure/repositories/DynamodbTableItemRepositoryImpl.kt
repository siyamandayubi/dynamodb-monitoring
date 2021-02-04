package com.siyamand.aws.dynamodb.infrastructure.repositories

import com.siyamand.aws.dynamodb.core.monitoring.entities.item.AttributeValueEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.item.TableItemEntity
import com.siyamand.aws.dynamodb.core.dynamodb.TableItemRepository
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilder
import com.siyamand.aws.dynamodb.infrastructure.mappers.CredentialMapper
import com.siyamand.aws.dynamodb.infrastructure.mappers.TableItemtMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst
import reactor.core.publisher.Mono
import reactor.core.publisher.Mono.fromFuture
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.QueryRequest

class DynamodbTableItemRepositoryImpl(private val clientBuilder: ClientBuilder) : TableItemRepository, AwsBaseRepositoryImpl() {
    override suspend fun add(tableItem: TableItemEntity): TableItemEntity {
        val db = getClient(clientBuilder::buildAsyncDynamodb)
        val request = TableItemtMapper.convertRequest(tableItem)
        val response = db.putItem(request)
        val returnValue = response.thenApply(TableItemtMapper::convertResponse)
        return fromFuture(returnValue).awaitFirst()
    }

    override fun getList(tableName: String, startKey: Map<String, AttributeValueEntity>?): Flow<TableItemEntity> {
        val db = getClient(clientBuilder::buildAsyncDynamodb)
        val requestBuilder = QueryRequest.builder().tableName(tableName)
        if (startKey != null) {
            requestBuilder.exclusiveStartKey(TableItemtMapper.convertKey(startKey))
        }
        val response = db.queryPaginator(requestBuilder.build())
        val returnValue = response.items().map { it ->
            val tableItemEntity = TableItemEntity(tableName)
            tableItemEntity.attributes.putAll(it.mapValues { entry -> TableItemtMapper.convertToAttributeValueEntity(entry.value) })
            tableItemEntity
        }

        return returnValue.asFlow()
    }

    override suspend fun getItem(tableName: String, key: Map<String, AttributeValueEntity>): List<TableItemEntity> {
        val db = getClient(clientBuilder::buildAsyncDynamodb)
        val query = convert(tableName, key)

        val response = db.query(query).thenApply {
            it.items().map { it ->
                val tableItemEntity = TableItemEntity(tableName)
                tableItemEntity.attributes.putAll(it.mapValues { entry -> TableItemtMapper.convertToAttributeValueEntity(entry.value) })
                tableItemEntity
            }
        }

        return fromFuture(response).awaitFirst()
    }

    private fun convert(tableName: String, key: Map<String, AttributeValueEntity>) :QueryRequest {
        val requestBuilder = QueryRequest.builder().tableName(tableName)
        val values = mutableMapOf<String, AttributeValue>()
        val expression = key.map {
            val expr = "${it.key} = :${it.key}"
            values.putIfAbsent(":${it.key}", TableItemtMapper.convertAttributeValue(it.value))
            expr
        }.joinToString(separator = " and ")

        requestBuilder.keyConditionExpression(expression)
        requestBuilder.expressionAttributeValues(values)

        return requestBuilder.build()
    }

    private fun asyncDynamoDb(): DynamoDbAsyncClient {
        requireNotNull(this.token) { "token is not provider" }

        if (this.region.isNullOrEmpty()) {
            throw java.lang.IllegalArgumentException("region is not provider")
        }

        val credential = CredentialMapper.convert(this.token!!)
        return clientBuilder.buildAsyncDynamodb(region, credential)
    }
}