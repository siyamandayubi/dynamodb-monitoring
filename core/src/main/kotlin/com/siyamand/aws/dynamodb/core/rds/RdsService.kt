package com.siyamand.aws.dynamodb.core.rds

import com.siyamand.aws.dynamodb.core.resource.ResourceEntity

interface RdsService {
    suspend fun createDbInstance(name: String): ResourceEntity
    suspend fun getList(marker: String): RdsListEntity
    suspend fun createProxy(rdsIdentifier: String, secretName: String): ResourceEntity
    suspend fun createDatabase(rdsIdentifier: String, secretName: String)
}