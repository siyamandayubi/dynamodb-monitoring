package com.siyamand.aws.dynamodb.core.monitoring

import com.siyamand.aws.dynamodb.core.sdk.dynamodb.CreateIndexEntity
import com.siyamand.aws.dynamodb.core.sdk.dynamodb.TableDetailEntity
import com.siyamand.aws.dynamodb.core.sdk.dynamodb.TableItemEntity

interface MonitoringTableBuilder {
    val keyName: String
    val resouceColumnName: String
    fun buildMonitoringMetadataTable(): TableDetailEntity
    fun buildIndexRequest(): CreateIndexEntity
    fun buildMonitoringResourceTable(): TableDetailEntity
    fun createResourceItem(monitoringId: String, arn: String): TableItemEntity
}