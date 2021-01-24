package com.siyamand.aws.dynamodb.core.services

import com.siyamand.aws.dynamodb.core.entities.RdsListEntity
import com.siyamand.aws.dynamodb.core.entities.ResourceEntity

interface RdsService {
    suspend fun createDbInstance(name: String): ResourceEntity
    suspend fun getList(marker: String): RdsListEntity
    suspend fun createProxy(rdsIdentifier: String, secretName: String): ResourceEntity
}