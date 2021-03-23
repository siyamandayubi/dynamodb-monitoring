package com.siyamand.aws.dynamodb.core.sdk.dynamodb


class CreateIndexEntity(val indexName: String, val keySchema: List<TableKeyScheme>) {
}

class IndexEntity(val indexName: String, val indexStatus: String, val keySchema: List<TableKeyScheme>)