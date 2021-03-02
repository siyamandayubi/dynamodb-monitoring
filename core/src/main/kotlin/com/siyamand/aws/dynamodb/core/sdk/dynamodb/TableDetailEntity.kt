package com.siyamand.aws.dynamodb.core.sdk.dynamodb

import com.siyamand.aws.dynamodb.core.sdk.resource.ResourceEntity

class TableDetailEntity(
        val arn: String,
        val tableName: String,
        val attributes: MutableList<TableAttribute>,
        val keySchema: MutableList<TableKeyScheme>,
        val status :String,
        val streamEnabled: Boolean,
        val latestStream: ResourceEntity?
) {
}

class TableAttribute(val attributeName: String, val attributeType: String)

class TableKeyScheme(val attributeName: String, val keyType: String)