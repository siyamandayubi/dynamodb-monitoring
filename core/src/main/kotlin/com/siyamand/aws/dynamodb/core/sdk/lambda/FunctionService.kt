package com.siyamand.aws.dynamodb.core.sdk.lambda

import com.siyamand.aws.dynamodb.core.common.PageResultEntity
import com.siyamand.aws.dynamodb.core.sdk.resource.ResourceEntity

interface FunctionService {
    suspend fun getFunctions(): List<FunctionEntity>
    suspend fun getDetail(name: String): FunctionDetailEntity?
    suspend fun addLambda(functionName: String, code: String): ResourceEntity
    suspend fun getLayers(marker: String): PageResultEntity<FunctionLayerListEntity>
    suspend fun getLayer(name: String): PageResultEntity<FunctionLayerEntity>
    suspend fun addLayer(layerName: String, path: String, description: String): FunctionLayerEntity
}