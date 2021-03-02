package com.siyamand.aws.dynamodb.core.monitoring

import com.siyamand.aws.dynamodb.core.common.MonitorConfigProvider
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.PrerequisteEntity
import com.siyamand.aws.dynamodb.core.sdk.role.RoleRepository

class PrerequisiteServiceImpl(
        private val monitorConfigProvider: MonitorConfigProvider,
        private val roleRepository: RoleRepository,
        private val metadataService: MetadataService) : PrerequisiteService {
    override suspend fun getPrerequistes(): PrerequisteEntity {
        val lambdaRole = roleRepository.getRole(monitorConfigProvider.getLambdaRole())
        val rdsRole = roleRepository.getRole(monitorConfigProvider.getRdsProxyRole())
        val table = metadataService.getMonitoringTable()

        return PrerequisteEntity(lambdaRole, rdsRole, table)
    }
}