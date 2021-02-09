package com.siyamand.aws.dynamodb.core.rds

import com.siyamand.aws.dynamodb.core.resource.ResourceEntity
import com.siyamand.aws.dynamodb.core.common.AWSBaseRepository

interface RdsRepository : AWSBaseRepository {
    suspend fun getRds(name: String):  List<RdsEntity>
    suspend fun createProxy(entity: CreateProxyEntity): ResourceEntity
    suspend fun createRds(entity: CreateDbInstanceEntity): RdsEntity
    suspend fun registerDbProxyTarget(entity: CreateDbProxyTargetEntity): List<DbProxyTargetEntity>
    suspend fun list(marker: String): RdsListEntity
}