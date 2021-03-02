package com.siyamand.aws.dynamodb.core.sdk.secretManager

import com.siyamand.aws.dynamodb.core.sdk.resource.ResourceEntity
import com.siyamand.aws.dynamodb.core.sdk.resource.TagEntity
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

class  SecretDetailEntity(
        val name: String,
        val resourceEntity: ResourceEntity,
        val createdDate: Instant?,
        val lastAccessDate:Instant?,
        val deletionDate: Instant?,
        val description: String,
        val tags: List<TagEntity>
)