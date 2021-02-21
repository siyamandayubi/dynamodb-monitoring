package com.siyamand.aws.dynamodb.core.monitoring

import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.PrerequisteEntity
import com.siyamand.aws.dynamodb.core.role.RoleService

class PrerequisiteServiceImpl(private val roleService: RoleService, private val metadataService: MetadataService) : PrerequisiteService {
    override suspend fun getPrerequistes(): PrerequisteEntity {
        val role = roleService.getLambdaRole()
        val table = metadataService.getMonitoringTable()

        return PrerequisteEntity(role, table)
    }
}