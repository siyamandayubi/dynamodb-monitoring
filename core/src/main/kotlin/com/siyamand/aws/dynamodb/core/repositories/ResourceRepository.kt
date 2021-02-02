package com.siyamand.aws.dynamodb.core.repositories

import com.siyamand.aws.dynamodb.core.entities.PageResultEntity
import com.siyamand.aws.dynamodb.core.entities.ResourceEntity

interface ResourceRepository : AWSBaseRepository {
    fun getResources(tagName: String, tagValue: String, types: Array<String>?, nextPageToken: String?): PageResultEntity<ResourceEntity>
}