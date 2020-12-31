package com.siyamand.aws.dynamodb.infrastructure.tests.repositories

import com.siyamand.aws.dynamodb.core.entities.TokenCredentialEntity
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilder
import com.siyamand.aws.dynamodb.infrastructure.repositories.DynamodbTableRepositoryImpl
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.model.*
import java.util.concurrent.CompletableFuture
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ExtendWith(SpringExtension::class)
class DynamodbTableRepositoryImplTest {
    @Test
    fun testGetDetail_CheckOutput() {
        val clientBuilder: ClientBuilder = mockk<ClientBuilder>()
        val attributes = listOf<AttributeDefinition>(
                AttributeDefinition.builder().attributeName("attr1").attributeType("S").build(),
                AttributeDefinition.builder().attributeName("attr2").attributeType("N").build());
        val keySchemas = listOf<KeySchemaElement>(
                KeySchemaElement.builder().attributeName("attr1").keyType(KeyType.HASH).build(),
                KeySchemaElement.builder().attributeName("attr2").keyType(KeyType.HASH).build());
        val describeTableResponse: DescribeTableResponse = DescribeTableResponse
                .builder()
                .table(TableDescription
                        .builder()
                        .attributeDefinitions(attributes)
                        .keySchema(keySchemas)
                        .build())
                .build()

        every { clientBuilder.buildAsyncDynamodb(any(), any()).describeTable(any<DescribeTableRequest>()) } returns CompletableFuture.completedFuture(describeTableResponse)
        val dynamodbTableRepository: DynamodbTableRepositoryImpl = DynamodbTableRepositoryImpl(clientBuilder)
        dynamodbTableRepository.initialize(TokenCredentialEntity("", "", "", null),"us-east-2")

        val result = runBlocking { dynamodbTableRepository.getDetail("test") }
        if (result == null) {
            throw Exception("Result is null")
        } else {
            assertEquals(result.attributes.size, 2, "have two attributes in output")
            assertEquals(result.attributes[0].attributeName, attributes[0].attributeName())
            assertEquals(result.attributes[0].attributeType, attributes[0].attributeType())
            assertEquals(result.attributes[1].attributeName, attributes[1].attributeName())
            assertEquals(result.attributes[1].attributeType, attributes[1].attributeType())
        }
    }

    @Test
    suspend fun testGetList_Check_Output() {
        val clientBuilder: ClientBuilder = mockk<ClientBuilder>()
        val dynamoDbAsyncClient = mockk<DynamoDbAsyncClient>()
        val completableFuture = CompletableFuture<ListTablesResponse>()
        completableFuture.complete(ListTablesResponse.builder().tableNames("table1", "table2").build())
        every { clientBuilder.buildAsyncDynamodb(any(), any()).listTables() } returns completableFuture


        val dynamodbTableRepository: DynamodbTableRepositoryImpl = DynamodbTableRepositoryImpl(clientBuilder)
        dynamodbTableRepository.initialize(TokenCredentialEntity("", "", "", null),"us-east-2")

        val tables = dynamodbTableRepository.getList()
        assertEquals(tables.size, 2, "output size is wrong")
        assertEquals(tables[0].name, "table1", "item one not match")
        assertEquals(tables[1].name, "table2", "item two not match")
    }

    @Test
    fun testGetList_Check_regionEmptyMustThrowIllegalArgumentException() {
        val clientBuilder: ClientBuilder = mockk<ClientBuilder>()
        val dynamodbTableRepository: DynamodbTableRepositoryImpl = DynamodbTableRepositoryImpl(clientBuilder)
        dynamodbTableRepository.initialize(TokenCredentialEntity("", "", "", null),"us-east-2")

        val exception: Exception = assertThrows { GlobalScope.launch { dynamodbTableRepository.getList() } }

        assertNotNull(exception, "empty region must throw an exception")
        assert(exception is IllegalArgumentException)
    }

    @Test
    fun testGetList_Check_noTokenMustThrowIllegalArgumentException() {
        val clientBuilder: ClientBuilder = mockk<ClientBuilder>()
        val dynamodbTableRepository: DynamodbTableRepositoryImpl = DynamodbTableRepositoryImpl(clientBuilder)

        val exception: Exception = assertThrows { GlobalScope.launch { dynamodbTableRepository.getList() } }

        assertNotNull(exception, "empty region must throw an exception")
        assert(exception is IllegalArgumentException)
    }
}