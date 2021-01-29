package com.siyamand.aws.dynamodb.infrastructure.repositories

import com.siyamand.aws.dynamodb.core.repositories.VpcRepository
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilder
import software.amazon.awssdk.services.ec2.model.*

class VpcRepositoryImpl(private val clientBuilder: ClientBuilder) : VpcRepository, AwsBaseRepositoryImpl() {
    override fun getSubnets(vpcs: List<String>): List<String> {
        val client = getClient(clientBuilder::buildEc2Client)
        val response = client.describeSubnets(
                DescribeSubnetsRequest
                        .builder()
                        .filters(Filter
                                .builder()
                                .name("vpc-id")
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