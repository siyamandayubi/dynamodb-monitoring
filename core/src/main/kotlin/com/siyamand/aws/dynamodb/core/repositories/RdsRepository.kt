package com.siyamand.aws.dynamodb.core.repositories

import com.siyamand.aws.dynamodb.core.entities.RdsEntity
import com.siyamand.aws.dynamodb.core.entities.RdsListEntity
import com.siyamand.aws.dynamodb.core.entities.ResourceEntity
import com.siyamand.aws.dynamodb.core.entities.database.CreateDbInstanceEntity
import com.siyamand.aws.dynamodb.core.entities.database.CreateDbProxyTargetEntity
import com.siyamand.aws.dynamodb.core.entities.database.CreateProxyEntity
import com.siyamand.aws.dynamodb.core.entities.database.DbProxyTargetEntity

interface RdsRepository : AWSBaseRepository {
    suspend fun getRds(name: String):  List<RdsEntity>
    suspend fun createProxy(entity: CreateProxyEntity): ResourceEntity
    suspend fun createDatabase(entity: CreateDbInstanceEntity): ResourceEntity
    suspend fun registerDbProxyTarget(entity: CreateDbProxyTargetEntity): List<DbProxyTargetEntity>
    suspend fun list(marker: String): RdsListEntity
}