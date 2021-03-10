package com.siyamand.aws.dynamodb.core.sdk.rds

import com.siyamand.aws.dynamodb.core.common.AWSBaseRepository
import com.siyamand.aws.dynamodb.core.common.PageResultEntity
import com.siyamand.aws.dynamodb.core.sdk.rds.entities.CreateDbInstanceEntity
import com.siyamand.aws.dynamodb.core.sdk.rds.entities.*

interface RdsRepository : AWSBaseRepository {
    suspend fun getRds(vararg name: String): List<RdsEntity>
    suspend fun createProxy(entity: CreateProxyEntity): RdsProxyEntity
    suspend fun createRds(entity: CreateDbInstanceEntity): RdsEntity
    suspend fun registerDbProxyTarget(entity: CreateDbProxyTargetEntity): List<DbProxyTargetEntity>
    suspend fun list(marker: String): RdsListEntity
    suspend fun getProxy(name: String): PageResultEntity<RdsProxyEntity>
    suspend fun getDbProxyTargetGroups(dbProxyName: String): PageResultEntity<DbProxyTargetGroupEntity>
    suspend fun getDbProxyTargets(targetGroupName: String, dbProxyName: String): PageResultEntity<DbProxyTargetEntity>
    suspend fun listProxies(marker: String): PageResultEntity<RdsProxyEntity>
}