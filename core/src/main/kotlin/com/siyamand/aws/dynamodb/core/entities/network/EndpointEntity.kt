package com.siyamand.aws.dynamodb.core.entities.network

import com.siyamand.aws.dynamodb.core.entities.ErrorEntity
import com.siyamand.aws.dynamodb.core.entities.TagEntity
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