package com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring

import com.siyamand.aws.dynamodb.core.dynamodb.TableDetailEntity
import com.siyamand.aws.dynamodb.core.role.RoleEntity

class PrerequisteEntity(val lambdaRole: RoleEntity?, val monitoringTable: TableDetailEntity?) {
}