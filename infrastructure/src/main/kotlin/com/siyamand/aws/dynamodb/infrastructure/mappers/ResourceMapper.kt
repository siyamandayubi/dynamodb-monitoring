package com.siyamand.aws.dynamodb.infrastructure.mappers

import com.siyamand.aws.dynamodb.core.entities.ResourceEntity
import com.siyamand.aws.dynamodb.core.entities.TagEntity
import software.amazon.awssdk.arns.Arn
import software.amazon.awssdk.services.resourcegroupstaggingapi.model.ResourceTagMapping

class ResourceMapper {
    companion object {
        fun convert(resourceTagMapping: ResourceTagMapping): ResourceEntity {
            val arn = Arn.fromString(resourceTagMapping.resourceARN())
            val returnValue = ResourceEntity(arn.region().get(), arn.service(), arn.accountId().get(), arn.resourceAsString())
            returnValue.tags.addAll(resourceTagMapping.tags().map { TagEntity(it.key(),it.value()) })

            return returnValue
        }
    }
}