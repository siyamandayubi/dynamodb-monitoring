package com.siyamand.aws.dynamodb.infrastructure.tests.repositories

import com.siyamand.aws.dynamodb.core.authentication.TokenCredentialEntity
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilder
import com.siyamand.aws.dynamodb.infrastructure.repositories.LambdaRepositoryImpl
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import software.amazon.awssdk.services.lambda.model.FunctionConfiguration
import software.amazon.awssdk.services.lambda.model.ListFunctionsResponse
import java.util.concurrent.CompletableFuture
import kotlin.test.assertEquals

@ExtendWith(SpringExtension::class)
class LambdaRepositoryTest {
    @Test
    fun testGetList_CheckOutput() {
        val clientBuilder: ClientBuilder = mockk<ClientBuilder>()

        val listFunctionsResponse = ListFunctionsResponse.builder().functions(
                FunctionConfiguration.builder().functionName("t1").build(), FunctionConfiguration.builder().functionName("t2").build())
                .build()
        every { clientBuilder.buildAsyncAwsLambda(any(), any()).listFunctions() } returns CompletableFuture.completedFuture(listFunctionsResponse)

        val lambdaRepository = LambdaRepositoryImpl(clientBuilder)
        lambdaRepository.initialize(TokenCredentialEntity("", "", "", null),"us-east-2")
        val result = runBlocking { lambdaRepository.getList() }
        assertEquals(result.size, 2)
        assertEquals(result[0].name, "t1")
        assertEquals(result[1].name, "t2")
    }
}