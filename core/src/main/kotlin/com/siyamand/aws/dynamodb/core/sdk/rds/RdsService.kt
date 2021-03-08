package com.siyamand.aws.dynamodb.core.sdk.rds

import com.siyamand.aws.dynamodb.core.sdk.rds.entities.RdsListEntity
import com.siyamand.aws.dynamodb.core.sdk.resource.ResourceEntity

interface RdsService {
    suspend fun createDbInstance(name: String): ResourceEntity
    suspend fun getList(marker: String): RdsListEntity
    suspend fun createProxy(rdsIdentifier: String, secretName: String): ResourceEntity
    suspend fun createDatabase(rdsIdentifier: String, secretName: String)
}