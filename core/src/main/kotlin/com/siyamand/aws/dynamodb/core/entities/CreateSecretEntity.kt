package com.siyamand.aws.dynamodb.core.entities

class CreateSecretEntity(val name: String, val description: String, val secretData: String, val tags: MutableList<TagEntity> = mutableListOf()) {
}