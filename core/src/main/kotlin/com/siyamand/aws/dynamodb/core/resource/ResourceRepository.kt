package com.siyamand.aws.dynamodb.core.resource

import com.siyamand.aws.dynamodb.core.common.PageResultEntity
import com.siyamand.aws.dynamodb.core.common.AWSBaseRepository

interface ResourceRepository : AWSBaseRepository {
    fun getResources(tagName: String, tagValue: String, types: Array<String>?, nextPageToken: String?): PageResultEntity<ResourceEntity>
    fun convert(arn: String): ResourceEntity
}