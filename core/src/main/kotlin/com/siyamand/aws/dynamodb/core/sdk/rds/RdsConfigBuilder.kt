package com.siyamand.aws.dynamodb.core.sdk.rds

import com.siyamand.aws.dynamodb.core.sdk.rds.entities.RdsAppConfigEntity
import com.siyamand.aws.dynamodb.core.sdk.rds.entities.RdsProxyEntity

interface RdsConfigBuilder {
    fun create(rdsProxies: List<RdsProxyEntity>, databaseName: String): RdsAppConfigEntity
}