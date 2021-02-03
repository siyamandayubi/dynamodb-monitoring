package com.siyamand.aws.dynamodb.core.network

interface VpcService {
    suspend fun getEndpoints(): List<EndpointEntity>
}