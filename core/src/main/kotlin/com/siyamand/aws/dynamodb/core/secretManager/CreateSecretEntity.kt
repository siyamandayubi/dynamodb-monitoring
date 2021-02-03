package com.siyamand.aws.dynamodb.core.secretManager

import com.siyamand.aws.dynamodb.core.resource.ResourceEntity
import com.siyamand.aws.dynamodb.core.resource.TagEntity
import java.time.Instant

class CreateSecretEntity(
        var name: String,
        val description: String,
        val secretData: String,
        val tags: MutableList<TagEntity> = mutableListOf()
) {
}

class  SecretEntity(
        val name: String,
        val secretData: String,
        val resourceEntity: ResourceEntity,
        val createdDate: Instant
)