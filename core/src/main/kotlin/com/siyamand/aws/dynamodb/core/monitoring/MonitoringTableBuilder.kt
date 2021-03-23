package com.siyamand.aws.dynamodb.core.monitoring

import com.siyamand.aws.dynamodb.core.sdk.dynamodb.CreateIndexEntity
import com.siyamand.aws.dynamodb.core.sdk.dynamodb.TableDetailEntity

interface MonitoringTableBuilder {
    val keyName: String
    fun build(tableName: String): TableDetailEntity
    fun buildIndexRequest(): CreateIndexEntity
}