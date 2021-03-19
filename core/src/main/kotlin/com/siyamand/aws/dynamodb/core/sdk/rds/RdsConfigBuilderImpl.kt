package com.siyamand.aws.dynamodb.core.sdk.rds

import com.siyamand.aws.dynamodb.core.sdk.rds.entities.RdsAppConfigEntity
import com.siyamand.aws.dynamodb.core.sdk.rds.entities.RdsAppConfigItemEntity
import com.siyamand.aws.dynamodb.core.sdk.rds.entities.RdsProxyEntity

class RdsConfigBuilderImpl : RdsConfigBuilder {
    override fun create(rdsProxies: List<RdsProxyEntity>, databaseName: String): RdsAppConfigEntity {
        if (!rdsProxies.any()) {
            return RdsAppConfigEntity()
        }

        if (rdsProxies.size > 4096) {
            throw Exception("Too many RDS instances")
        }

        val returnValue = RdsAppConfigEntity()
        returnValue.databaseName = databaseName

        val step = 4095 / rdsProxies.size
        var start = 0
        var order = 0
        returnValue.endpoints.addAll(rdsProxies.map {
            val item = RdsAppConfigItemEntity()
            item.arn = it.dbProxyResource.arn
            item.port = 3306
            item.endPoint = it.endpoint ?: ""
            item.order = order
            order++
            item.start = start.toString(16).padStart(3, '0')
            item.end = (start + step).toString(16).padStart(3, '0')
            start += step + 1

            item
        })

        return returnValue
    }
}