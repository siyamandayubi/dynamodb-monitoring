package com.siyamand.aws.dynamodb.infrastructure.mappers

import com.siyamand.aws.dynamodb.core.sdk.network.VpcEntity
import com.siyamand.aws.dynamodb.core.sdk.resource.TagEntity
import software.amazon.awssdk.services.ec2.model.Vpc

class VpcMapper {
    companion object {
        fun convert(vpc: Vpc): VpcEntity {
            val entity = VpcEntity()
            entity.instanceTenancy = vpc.instanceTenancyAsString()
            entity.cidrBlock = vpc.cidrBlock()
            entity.dhcpOptionsId = vpc.dhcpOptionsId()
            entity.vpcId = vpc.vpcId()
            entity.isDefault = vpc.isDefault
            entity.dhcpOptionsId = vpc.dhcpOptionsId()
            entity.ownerId = vpc.ownerId()
            entity.state = vpc.stateAsString()
            entity.tags = vpc.tags().map { TagEntity(it.key(), it.value()) }
            return entity
        }
    }
}