package com.siyamand.aws.dynamodb.core.entities

class TableDetailEntity(
        val attributes: MutableList<TableAttribute>,
        val keySchema: MutableList<TableKeyScheme>
) {
}

class TableAttribute(val attributeName: String, val attributeType: String)

class TableKeyScheme(val attributeName: String, val keyType: String)