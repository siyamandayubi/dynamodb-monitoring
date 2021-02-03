package com.siyamand.aws.dynamodb.infrastructure.mappers

import com.siyamand.aws.dynamodb.core.resource.ResourceEntity
import com.siyamand.aws.dynamodb.core.resource.TagEntity
import software.amazon.awssdk.arns.Arn
import software.amazon.awssdk.services.resourcegroupstaggingapi.model.ResourceTagMapping

class ResourceMapper {
    companion object {
        fun convert(resourceTagMapping: ResourceTagMapping): ResourceEntity {
            val returnValue = convert(resourceTagMapping.resourceARN())
            returnValue.tags.addAll(resourceTagMapping.tags().map { TagEntity(it.key(),it.value()) })

            return returnValue
        }

        fun convert(arnString:String):ResourceEntity{
            val arn = Arn.fromString(arnString)
            return ResourceEntity(arn.region().get(), arn.service(), arn.accountId().get(), arn.resourceAsString(), arnString)
        }
    }
}