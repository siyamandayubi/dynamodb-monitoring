package com.siyamand.aws.dynamodb.core.entities.item

class TableItemEntity(val tableName: String) {
    public val attributes : MutableMap<String, AttributeValueEntity> = mutableMapOf()
}