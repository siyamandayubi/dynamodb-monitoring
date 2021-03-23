package com.siyamand.aws.dynamodb.core.monitoring

import com.siyamand.aws.dynamodb.core.common.MonitorConfigProvider
import com.siyamand.aws.dynamodb.core.common.initializeRepositories
import com.siyamand.aws.dynamodb.core.common.initializeRepositoriesWithGlobalRegion
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.PrerequisteEntity
import com.siyamand.aws.dynamodb.core.sdk.authentication.CredentialEntity
import com.siyamand.aws.dynamodb.core.sdk.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.sdk.dynamodb.TableDetailEntity
import com.siyamand.aws.dynamodb.core.sdk.dynamodb.TableRepository
import com.siyamand.aws.dynamodb.core.sdk.role.RoleRepository
import com.siyamand.aws.dynamodb.core.sdk.role.RoleService

class PrerequisiteServiceImpl(
        private val monitorConfigProvider: MonitorConfigProvider,
        private val credentialProvider: CredentialProvider,
        private val roleRepository: RoleRepository,
        private val roleService: RoleService,
        private val tableRepository: TableRepository,
        private val monitoringTableBuilder: MonitoringTableBuilder) : PrerequisiteService {

    override suspend fun getPrerequistes(): PrerequisteEntity {

        credentialProvider.initializeRepositoriesWithGlobalRegion(roleRepository)
        val lambdaRole = roleRepository.getRole(monitorConfigProvider.getLambdaRole())
        val rdsRole = roleRepository.getRole(monitorConfigProvider.getRdsProxyRole())
        val table = getMonitoringTable()

        val index = table?.indexes?.firstOrNull { it.indexName == monitorConfigProvider.getMonitoringTableSourceTableIndexName() }
        return PrerequisteEntity(lambdaRole, rdsRole, table, index)
    }

    override suspend fun getMonitoringTable(): TableDetailEntity? {
        credentialProvider.initializeRepositories(tableRepository)
        val tableName = monitorConfigProvider.getMonitoringConfigMetadataTable()
        if (tableName.isNullOrEmpty()) {
            throw Exception("No config name for Monitoring Dynamodb table")
        }

        return tableRepository.getDetail(tableName)
    }

    override suspend fun createPrerequistes(credentialEntity: CredentialEntity): PrerequisteEntity {
        val lambdaRole = roleService.getOrCreateLambdaRole(credentialEntity)
        val rdsRole = roleService.getOrCreateRdsProxyRole(credentialEntity)
        var table = getOrCreateMonitoringTable()
        val index = table.indexes.firstOrNull { it.indexName == monitorConfigProvider.getMonitoringTableSourceTableIndexName() }
        if (index == null) {
            table = getOrCreateMonitoringTableIndexes()
        }

        return PrerequisteEntity(lambdaRole, rdsRole, table, index)
    }

    suspend fun getOrCreateMonitoringTableIndexes(): TableDetailEntity {
        credentialProvider.initializeRepositories(tableRepository)

        return tableRepository.addIndex(monitorConfigProvider.getMonitoringConfigMetadataTable(), monitoringTableBuilder.buildIndexRequest())
    }

    override suspend fun getOrCreateMonitoringTable(): TableDetailEntity {
        credentialProvider.initializeRepositories(tableRepository)

        val tableName = monitorConfigProvider.getMonitoringConfigMetadataTable()
        if (tableName.isNullOrEmpty()) {
            throw Exception("No config name for Monitoring Dynamodb table")
        }

        var table = tableRepository.getDetail(tableName)
        if (table != null) {
            return table
        }

        return tableRepository.add(monitoringTableBuilder.build(tableName))
    }
}