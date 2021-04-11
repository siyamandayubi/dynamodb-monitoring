package com.siyamand.aws.dynamodb.core.sdk.network

import com.siyamand.aws.dynamodb.core.common.ErrorEntity
import com.siyamand.aws.dynamodb.core.sdk.resource.TagEntity
import java.time.Instant

class EndpointEntity(
        val vpcEndpointId: String,
        val vpcEndpointType: String,
        val vpcId: String,
        val state: String,
        val policyDocument: String,
        val serviceName: String? = null,
        var dnsEnabled: Boolean? = null,
        var requesterManaged: Boolean? = null,
        var ownerId: String? = null,
        var creationTimestamp: Instant? = null) {

    var lastError: ErrorEntity? = null
    val routeTableIds: MutableList<String> = mutableListOf()

    val subnetIds: MutableList<String> = mutableListOf()

    val groups: MutableList<SecurityGroupEntity> = mutableListOf()

    var networkInterfaceIds: MutableList<String> = mutableListOf()

    val dnsEntries: MutableList<DnsEntity> = mutableListOf()

    val tags: MutableList<TagEntity> = mutableListOf()

}

class DnsEntity(val dns: String, val zoneId: String)

class SecurityGroupEntity(val groupName: String, val groupId: String)

class CreateEndpointEntity(
        val vpcEndpointType: String = "",
        val vpcId: String = "",
        val serviceName: String = "",
        val policyDocument: String = "",
        val clientToken: String = "",
        val dnsEnabled: Boolean = false
) {
    val routeTableIds: List<String> = mutableListOf()

    val subnetIds: List<String> = mutableListOf()

    val securityGroupIds: List<String> = mutableListOf()

    val tagSpecifications: MutableList<TagEntity>? = mutableListOf()
}

class InternetGatewayEntity(
        val ownerId: String,
        internetGatewayId: String,
        tags: List<TagEntity> = listOf(),
        attachments: List<InternetGatewayAttachmentEntity> = listOf()
)

class InternetGatewayAttachmentEntity(val vpcId: String, val status: String)

class VpcEntity {
    var cidrBlock: String? = null

    var dhcpOptionsId: String? = null

    var state: String? = null

    var vpcId: String = ""

    var ownerId: String? = null

    var instanceTenancy: String? = null

    var isDefault: Boolean? = null

    var tags: List<TagEntity> = listOf()
}

class RegionEntity(val name: String, val endpoint: String)