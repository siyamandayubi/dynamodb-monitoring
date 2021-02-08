package com.siyamand.aws.dynamodb.core.monitoring

import com.siyamand.aws.dynamodb.core.dynamodb.TableDetailEntity

interface MonitoringTableBuilder {
    fun build(tableName: String): TableDetailEntity
}