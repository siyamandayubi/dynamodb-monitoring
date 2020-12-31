package com.siyamand.aws.dynamodb.core.repositories

import com.siyamand.aws.dynamodb.core.entities.PageResult
import com.siyamand.aws.dynamodb.core.entities.ResourceEntity

interface ResourceRepository : AWSBaseRepository {
    fun getResources(tagName: String, tagValue: String, nextPageToken: String): PageResult<ResourceEntity>
}