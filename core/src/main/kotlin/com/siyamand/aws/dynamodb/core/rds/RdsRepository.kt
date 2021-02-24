package com.siyamand.aws.dynamodb.core.rds

import com.siyamand.aws.dynamodb.core.resource.ResourceEntity
import com.siyamand.aws.dynamodb.core.common.AWSBaseRepository
import com.siyamand.aws.dynamodb.core.common.PageResultEntity

interface RdsRepository : AWSBaseRepository {
    suspend fun getRds(name: String):  List<RdsEntity>
    suspend fun createProxy(entity: CreateProxyEntity): RdsProxyEntity
    suspend fun createRds(entity: CreateDbInstanceEntity): RdsEntity
    suspend fun registerDbProxyTarget(entity: CreateDbProxyTargetEntity): List<DbProxyTargetEntity>
    suspend fun list(marker: String): RdsListEntity
    suspend fun getProxies(name: String): PageResultEntity<RdsProxyEntity>
    suspend fun getDbProxyTargetGroups(dbProxyName: String): PageResultEntity<DbProxyTargetGroupEntity>
    suspend fun getDbProxyTargets(targetGroupName: String, dbProxyName: String): PageResultEntity<DbProxyTargetEntity>
}