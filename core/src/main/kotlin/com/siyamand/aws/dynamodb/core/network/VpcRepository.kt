package com.siyamand.aws.dynamodb.core.network

import com.siyamand.aws.dynamodb.core.common.PageResultEntity
import com.siyamand.aws.dynamodb.core.common.AWSBaseRepository

interface VpcRepository : AWSBaseRepository {
    fun getSubnets(vpc: List<String>): List<String>
    fun getVpcsBySecurityGroup(vpcGroupId: String): List<String>
    fun getSecurityGroupVpcs(groupIds: List<String>?): List<String>
    fun getEndpoints(nextToken: String?, vpcList: List<String>?) : PageResultEntity<EndpointEntity>
    fun createEndpoint(entity: CreateEndpointEntity): EndpointEntity
    fun createInternetGateway(tags: Map<String, String>): InternetGatewayEntity
}