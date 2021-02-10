package com.siyamand.aws.dynamodb.core.dynamodb

import java.time.Instant

class TableItemEntity(val tableName: String) {
    public val attributes: MutableMap<String, AttributeValueEntity> = mutableMapOf()
    public val key : MutableMap<String, AttributeValueEntity> = mutableMapOf()
}

class TableItemReaderDecorator(private val tableItemEntity: Map<String, AttributeValueEntity>) {
    fun str(name: String): String {
        if (!tableItemEntity.containsKey(name)) {
            return ""
        }

        val attr = tableItemEntity[name]
        if (attr?.type != AttributeValueType.STRING) {
            return ""
        }

        return attr.stringValue ?: ""
    }

    fun int(name: String): Int? {
        if (!tableItemEntity.containsKey(name)) {
            return null
        }

        val attr = tableItemEntity[name]
        if (attr?.type != AttributeValueType.INT) {
            return null
        }

        return attr?.intValue
    }

    fun instant(name: String): Instant {
        if (!tableItemEntity.containsKey(name)) {
            return Instant.MIN
        }

        val attr = tableItemEntity[name]
        if (attr?.type != AttributeValueType.DATE) {
            return Instant.MIN
        }

        return attr?.instantValue ?: Instant.MIN
    }

    fun complex(name: String): TableItemReaderDecorator {
        if (!tableItemEntity.containsKey(name)) {
            return TableItemReaderDecorator(mapOf())
        }

        val attr = tableItemEntity[name]
        if (attr?.type != AttributeValueType.COMPLEX) {
            return TableItemReaderDecorator(mapOf())
        }

        return TableItemReaderDecorator(attr?.complexValue ?: mapOf())
    }
}