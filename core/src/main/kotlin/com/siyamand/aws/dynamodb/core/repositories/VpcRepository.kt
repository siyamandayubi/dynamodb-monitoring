package com.siyamand.aws.dynamodb.core.repositories

import com.siyamand.aws.dynamodb.core.entities.PageResultEntity
import com.siyamand.aws.dynamodb.core.entities.RdsEntity
import com.siyamand.aws.dynamodb.core.entities.RdsListEntity
import com.siyamand.aws.dynamodb.core.entities.ResourceEntity
import com.siyamand.aws.dynamodb.core.entities.database.CreateDbInstanceEntity
import com.siyamand.aws.dynamodb.core.entities.database.CreateDbProxyTargetEntity
import com.siyamand.aws.dynamodb.core.entities.database.CreateProxyEntity
import com.siyamand.aws.dynamodb.core.entities.database.DbProxyTargetEntity
import com.siyamand.aws.dynamodb.core.entities.network.EndpointEntity

interface VpcRepository : AWSBaseRepository {
    fun getSubnets(vpc: List<String>): List<String>
    fun getVpcsBySecurityGroup(vpcGroupId: String): List<String>
    fun getSecurityGroupVpcs(groupIds: List<String>?): List<String>
    fun getEndpoints(nextToken: String?, vpcList: List<String>?) : PageResultEntity<EndpointEntity>
}