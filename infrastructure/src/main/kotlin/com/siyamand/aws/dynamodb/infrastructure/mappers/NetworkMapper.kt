package com.siyamand.aws.dynamodb.infrastructure.mappers

import com.siyamand.aws.dynamodb.core.network.InternetGatewayAttachmentEntity
import com.siyamand.aws.dynamodb.core.network.InternetGatewayEntity
import com.siyamand.aws.dynamodb.core.resource.TagEntity
import software.amazon.awssdk.services.ec2.model.CreateInternetGatewayResponse

class NetworkMapper {
    companion object {
        fun convert(response: CreateInternetGatewayResponse): InternetGatewayEntity {
            val gateway = response.internetGateway()
            return InternetGatewayEntity(
                    gateway.ownerId(),
                    gateway.internetGatewayId(),
                    tags = gateway.tags().map { TagEntity(it.key(), it.value()) },
                    attachments = gateway.attachments().map { InternetGatewayAttachmentEntity(it.vpcId(), it.state().name) })
        }
    }
}