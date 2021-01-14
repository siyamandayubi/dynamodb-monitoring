package com.siyamand.aws.dynamodb.core.entities.database

import com.siyamand.aws.dynamodb.core.entities.TagEntity

class CreateDbInstanceEntity(
        val dbName: String,
        val instanceName: String,
        val masterUsername: String,
        val masterPassword: String,
        val engine: String,
        val engineVersion: String,
        val availabilityZone: String,
        val tags: MutableList<TagEntity> = mutableListOf()) {
}