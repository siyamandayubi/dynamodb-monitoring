package com.siyamand.aws.dynamodb.core.services

import com.siyamand.aws.dynamodb.core.entities.network.EndpointEntity

interface VpcService {
    suspend fun getEndpoints(): List<EndpointEntity>
}