package com.siyamand.aws.dynamodb.core.builders

import com.siyamand.aws.dynamodb.core.entities.TableItemEntity

interface MetadataTableItemBuilder {
    fun build():TableItemEntity
}