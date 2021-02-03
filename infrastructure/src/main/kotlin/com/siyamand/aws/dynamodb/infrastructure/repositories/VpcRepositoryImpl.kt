package com.siyamand.aws.dynamodb.infrastructure.repositories

import com.siyamand.aws.dynamodb.core.entities.PageResultEntity
import com.siyamand.aws.dynamodb.core.entities.network.CreateEndpointEntity
import com.siyamand.aws.dynamodb.core.entities.network.EndpointEntity
import com.siyamand.aws.dynamodb.core.repositories.VpcRepository
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilder
import com.siyamand.aws.dynamodb.infrastructure.mappers.EndpointMapper
import software.amazon.awssdk.services.ec2.model.*

class VpcRepositoryImpl(private val clientBuilder: ClientBuilder) : VpcRepository, AwsBaseRepositoryImpl() {
    companion object {
        val VPC_ID_FILTER_NAME = "vpc-id"
    }

    override fun getSubnets(vpcs: List<String>): List<String> {
        val client = getClient(clientBuilder::buildEc2Client)
        val response = client.describeSubnets(
                DescribeSubnetsRequest
                        .builder()
                        .filters(Filter
                                .builder()
                                .name(VPC_ID_FILTER_NAME)
                                .values(vpcs)
                                .build())
                        .build())

        return response.subnets().map { it.subnetId() }
    }

    override fun getSecurityGroupVpcs(groupIds: List<String>?): List<String> {
        val client = getClient(clientBuilder::buildEc2Client)
        val requestBuilder = DescribeSecurityGroupsRequest.builder()
        if (groupIds != null && groupIds.any()) {
            requestBuilder.groupIds(groupIds)
        }
        val response = client.describeSecurityGroups(requestBuilder.build())
        return response.securityGroups().map { it.vpcId() }
    }

    override fun createEndpoint(entity: CreateEndpointEntity): EndpointEntity {
        val client = getClient(clientBuilder::buildEc2Client)
        val response = client.createVpcEndpoint(EndpointMapper.convert(entity))
        if (response.clientToken() != entity.clientToken) {
            throw Exception("ClientToken mismatch")
        }

        return EndpointMapper.convert(response.vpcEndpoint())
    }

    override fun getEndpoints(nextToken: String?, vpcList: List<String>?): PageResultEntity<EndpointEntity> {
        val client = getClient(clientBuilder::buildEc2Client)
        val requestBuilder = DescribeVpcEndpointsRequest.builder()

        if (vpcList != null && vpcList.any()) {
            requestBuilder
                    .filters(
                            Filter.builder()
                                    .name(VPC_ID_FILTER_NAME)
                                    .values(vpcList)
                                    .build())
        }
        if (!nextToken.isNullOrEmpty()) {
            requestBuilder.nextToken(nextToken)
        }
        val response = client.describeVpcEndpoints(requestBuilder.build())
        return PageResultEntity(response.vpcEndpoints().map(EndpointMapper::convert), response.nextToken())
    }

    override fun getVpcsBySecurityGroup(vpcGroupId: String): List<String> {
        val client = getClient(clientBuilder::buildEc2Client)
        val response = client.describeSecurityGroupReferences(
                DescribeSecurityGroupReferencesRequest
                        .builder()
                        .groupId(vpcGroupId)
                        .build())

        return response.securityGroupReferenceSet().map { it.referencingVpcId() }
    }
}