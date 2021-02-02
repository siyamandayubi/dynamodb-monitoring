package com.siyamand.aws.dynamodb.infrastructure.mappers

import com.siyamand.aws.dynamodb.core.entities.network.DnsEntity
import com.siyamand.aws.dynamodb.core.entities.network.EndpointEntity
import com.siyamand.aws.dynamodb.core.entities.network.SecurityGroupEntity
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
    }
}