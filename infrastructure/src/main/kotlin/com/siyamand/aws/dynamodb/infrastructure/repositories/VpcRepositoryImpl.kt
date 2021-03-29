package com.siyamand.aws.dynamodb.infrastructure.repositories

import com.siyamand.aws.dynamodb.core.common.PageResultEntity
import com.siyamand.aws.dynamodb.core.sdk.network.*
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilder
import com.siyamand.aws.dynamodb.infrastructure.mappers.EndpointMapper
import com.siyamand.aws.dynamodb.infrastructure.mappers.NetworkMapper
import com.siyamand.aws.dynamodb.infrastructure.mappers.VpcMapper
import software.amazon.awssdk.services.ec2.model.*

class VpcRepositoryImpl(private val clientBuilder: ClientBuilder) : VpcRepository, AwsBaseRepositoryImpl() {
    companion object {
        const val VPC_ID_FILTER_NAME = "vpc-id"
    }

    override fun getSubnets(vpc: List<String>): List<String> {
        val client = getClient(clientBuilder::buildEc2Client)
        val response = client.describeSubnets(
                DescribeSubnetsRequest
                        .builder()
                        .filters(Filter
                                .builder()
                                .name(VPC_ID_FILTER_NAME)
                                .values(vpc)
                                .build())
                        .build())

        return response.subnets().map { it.subnetId() }
    }

    override fun getSecurityGroupVpcs(groupIds: List<String>): List<String> {
        val client = getClient(clientBuilder::buildEc2Client)
        val requestBuilder = DescribeSecurityGroupsRequest.builder()
        if (groupIds.any()) {
            requestBuilder.groupIds(groupIds)
        }

        val response = client.describeSecurityGroups(requestBuilder.build())
        return response.securityGroups().map { it.vpcId() }
    }

    override fun getSecurityGroupsByVpcs(vpcIds: List<String>): List<String> {
        val client = getClient(clientBuilder::buildEc2Client)
        val requestBuilder = DescribeSecurityGroupsRequest.builder()

        if (vpcIds.any()) {
            requestBuilder.filters(Filter.builder().name(VPC_ID_FILTER_NAME).values(vpcIds).build())
        }

        val response = client.describeSecurityGroups(requestBuilder.build())
        return response.securityGroups().map { it.groupId() }
    }

    override fun getVpcs(isDefault: Boolean, vpcIds: List<String>): PageResultEntity<VpcEntity> {
        val client = getClient(clientBuilder::buildEc2Client)
        val requestBuilder = DescribeVpcsRequest.builder()
        if (vpcIds.any()) {
            requestBuilder.vpcIds(vpcIds)
        }

        if (isDefault) {
            requestBuilder.filters(Filter.builder().name("isDefault").values("true").build())
        }
        val response = client.describeVpcs(requestBuilder.build())
        return PageResultEntity(response.vpcs().map(VpcMapper::convert), response.nextToken() ?: "")
    }

    override fun createEndpoint(entity: CreateEndpointEntity): EndpointEntity {
        val client = getClient(clientBuilder::buildEc2Client)
        val response = client.createVpcEndpoint(EndpointMapper.convert(entity))
        if (response.clientToken() != entity.clientToken) {
            throw Exception("ClientToken mismatch")
        }

        return EndpointMapper.convert(response.vpcEndpoint())
    }

    override fun createInternetGateway(tags: Map<String, String>): InternetGatewayEntity {
        val client = getClient(clientBuilder::buildEc2Client)
        val response = client
                .createInternetGateway(
                        CreateInternetGatewayRequest
                                .builder()
                                .tagSpecifications(
                                        TagSpecification
                                                .builder()
                                                .tags(tags.map { Tag.builder().key(it.key).value(it.value).build() })
                                                .build())
                                .build())

        return NetworkMapper.convert(response)
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