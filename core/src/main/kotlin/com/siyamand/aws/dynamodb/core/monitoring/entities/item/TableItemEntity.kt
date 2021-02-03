package com.siyamand.aws.dynamodb.core.monitoring.entities.item

class TableItemEntity(val tableName: String) {
    public val attributes : MutableMap<String, AttributeValueEntity> = mutableMapOf()
}