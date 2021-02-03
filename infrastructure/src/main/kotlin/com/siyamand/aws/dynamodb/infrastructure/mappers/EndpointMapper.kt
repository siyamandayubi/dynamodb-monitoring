package com.siyamand.aws.dynamodb.infrastructure.mappers

import com.siyamand.aws.dynamodb.core.network.CreateEndpointEntity
import com.siyamand.aws.dynamodb.core.network.DnsEntity
import com.siyamand.aws.dynamodb.core.network.EndpointEntity
import com.siyamand.aws.dynamodb.core.network.SecurityGroupEntity
import software.amazon.awssdk.services.ec2.model.CreateVpcEndpointRequest
import software.amazon.awssdk.services.ec2.model.VpcEndpoint

class EndpointMapper {
    companion object {
        fun convert(endpoint: VpcEndpoint): EndpointEntity {
            val endpointEntity = EndpointEntity(
                    endpoint.vpcEndpointId()!!,
                    endpoint.vpcEndpointType()!!.name,
                    endpoint.vpcId()!!,
                    endpoint.state()!!.name,
                    endpoint.policyDocument()!!,
                    endpoint.serviceName()!!,
                    endpoint.privateDnsEnabled(),
                    endpoint.requesterManaged(),
                    endpoint.ownerId(),
                    endpoint.creationTimestamp())

            endpointEntity.routeTableIds.addAll(endpoint.routeTableIds()!!)
            endpointEntity.subnetIds.addAll(endpoint.subnetIds()!!)
            endpointEntity.groups.addAll(endpoint.groups()!!.map { SecurityGroupEntity(it.groupName()!!, it.groupId()!!) })
            endpointEntity.networkInterfaceIds.addAll(endpoint.networkInterfaceIds()!!)
            endpointEntity.dnsEntries.addAll(endpoint.dnsEntries()!!.map { DnsEntity(it.dnsName()!!, it.hostedZoneId()!!) })

            return endpointEntity
        }

        fun convert(entity: CreateEndpointEntity): CreateVpcEndpointRequest {
            val builder = CreateVpcEndpointRequest
                    .builder()
                    .clientToken(entity.clientToken)
                    .policyDocument(entity.policyDocument)
                    .privateDnsEnabled(entity.dnsEnabled)
                    .vpcId(entity.vpcId)
                    .vpcEndpointType(entity.vpcEndpointType);

            if (!entity.serviceName.isNullOrEmpty()) {
                builder.serviceName(entity.serviceName)
            }

            if(entity.routeTableIds.any()) {
                builder.routeTableIds(entity.routeTableIds)
            }

            if(entity.securityGroupIds.any()) {
                builder.securityGroupIds(entity.securityGroupIds)
            }

            return builder.build()
        }
    }
}