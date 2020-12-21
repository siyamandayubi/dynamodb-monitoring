package com.siyamand.aws.dynamodb.core.entities

class TableDetailEntity(
        val arn: String,
        val tableName: String,
        val attributes: MutableList<TableAttribute>,
        val keySchema: MutableList<TableKeyScheme>,
        val tableStatus: String
) {
}

class TableAttribute(val attributeName: String, val attributeType: String)

class TableKeyScheme(val attributeName: String, val keyType: String)