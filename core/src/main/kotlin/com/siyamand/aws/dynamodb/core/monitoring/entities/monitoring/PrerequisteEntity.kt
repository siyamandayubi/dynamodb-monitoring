package com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring

import com.siyamand.aws.dynamodb.core.sdk.dynamodb.IndexEntity
import com.siyamand.aws.dynamodb.core.sdk.dynamodb.TableDetailEntity
import com.siyamand.aws.dynamodb.core.sdk.role.RoleEntity

class PrerequisteEntity(
        val lambdaRole: RoleEntity?,
        val rdsRole: RoleEntity?,
        val monitoringTable: TableDetailEntity?,
        val monitoringTableSourceTableIndex: IndexEntity?) {
}