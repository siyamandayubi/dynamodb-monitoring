package com.siyamand.aws.dynamodb.core.sdk.network

interface VpcService {
    suspend fun getEndpoints(): List<EndpointEntity>
}