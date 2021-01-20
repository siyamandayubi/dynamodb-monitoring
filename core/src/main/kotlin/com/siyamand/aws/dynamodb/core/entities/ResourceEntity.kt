package com.siyamand.aws.dynamodb.core.entities

class ResourceEntity(val region: String, val service: String, val accountId: String, val resource: String, val arn: String) {
    val tags: MutableList<TagEntity> = mutableListOf()
}

class TagEntity(val name: String, val value: String)

enum class ResourceType(val value: String) {
    ROLE("role"),
    RDS("rds"),
    SECRET_MANAGER("secretsmanager")
}