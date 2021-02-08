package com.siyamand.aws.dynamodb.core.dynamodb

class TableDetailEntity(
        val arn: String,
        val tableName: String,
        val attributes: MutableList<TableAttribute>,
        val keySchema: MutableList<TableKeyScheme>,
        val status :String
) {
}

class TableAttribute(val attributeName: String, val attributeType: String)

class TableKeyScheme(val attributeName: String, val keyType: String)