package com.siyamand.aws.dynamodb.infrastructure.repositories

import com.siyamand.aws.dynamodb.core.common.PageResultEntityBase
import com.siyamand.aws.dynamodb.core.dynamodb.AttributeValueEntity
import com.siyamand.aws.dynamodb.core.dynamodb.TableItemEntity
import com.siyamand.aws.dynamodb.core.dynamodb.TableItemRepository
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilder
import com.siyamand.aws.dynamodb.infrastructure.mappers.CredentialMapper
import com.siyamand.aws.dynamodb.infrastructure.mappers.TableItemtMapper
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst
import reactor.core.publisher.Mono.fromFuture
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.QueryRequest
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest

class DynamodbTableItemRepositoryImpl(private val clientBuilder: ClientBuilder) : TableItemRepository, AwsBaseRepositoryImpl() {
    override suspend fun add(tableItem: TableItemEntity): TableItemEntity {
        val db = getClient(clientBuilder::buildAsyncDynamodb)
        val request = TableItemtMapper.convertRequest(tableItem)
        val response = db.putItem(request)
        val returnValue = response.thenApply { TableItemtMapper.convertResponse(tableItem.tableName, it) }
        return fromFuture(returnValue).awaitFirst()
    }

    override suspend fun getList(tableName: String, startKey: Map<String, AttributeValueEntity>?): PageResultEntityBase<TableItemEntity, Map<String, AttributeValueEntity>> {
        val db = getClient(clientBuilder::buildAsyncDynamodb)
        val requestBuilder1 = QueryRequest.builder().tableName(tableName)
        if (startKey != null) {
            requestBuilder1.exclusiveStartKey(TableItemtMapper.convertKey(startKey))
        }

        val requestBuilder = QueryRequest.builder()
        if (startKey != null && startKey.any()) {
            requestBuilder.exclusiveStartKey(TableItemtMapper.convertKey(startKey))
        }
        val response = db.query(QueryRequest.builder().build()).thenApply { res ->
            val items = res.items().map {
                val tableItemEntity = TableItemEntity(tableName)
                tableItemEntity.attributes.putAll(it.mapValues { entry -> TableItemtMapper.convertToAttributeValueEntity(entry.value) })
                tableItemEntity
            }
            PageResultEntityBase(items, res.lastEvaluatedKey().mapValues { TableItemtMapper.convertToAttributeValueEntity(it.value) })
        }

        return fromFuture(response).awaitFirst()
    }

    override suspend fun update(entity: TableItemEntity): TableItemEntity {
        val db = getClient(clientBuilder::buildAsyncDynamodb)
        val request = TableItemtMapper.convertUpdateRequest(entity)
        val response = db.updateItem(request).thenApply {
            val tableItemEntity = TableItemEntity(entity.tableName)
            tableItemEntity.attributes.putAll(it.attributes().mapValues { entry -> TableItemtMapper.convertToAttributeValueEntity(entry.value) })
            tableItemEntity
        }

        return fromFuture(response).awaitFirst()
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

    private fun convert(tableName: String, key: Map<String, AttributeValueEntity>): QueryRequest {
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
}