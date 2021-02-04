package com.siyamand.aws.dynamodb.core.dynamodb

class TableItemEntity(val tableName: String) {
    public val attributes : MutableMap<String, AttributeValueEntity> = mutableMapOf()
}