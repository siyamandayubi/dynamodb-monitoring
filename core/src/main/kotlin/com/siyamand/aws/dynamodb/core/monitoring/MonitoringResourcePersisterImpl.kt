package com.siyamand.aws.dynamodb.core.monitoring

import com.siyamand.aws.dynamodb.core.common.MonitorConfigProvider
import com.siyamand.aws.dynamodb.core.common.initializeRepositories
import com.siyamand.aws.dynamodb.core.sdk.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.sdk.dynamodb.TableItemRepository

class MonitoringResourcePersisterImpl(
        private var credentialProvider: CredentialProvider,
        private val monitorConfigProvider: MonitorConfigProvider,
        private val tableItemRepository: TableItemRepository,
        private val monitoringTableBuilder: MonitoringTableBuilder) : MonitoringResourcePersister {

    override suspend fun threadSafe() {
        this.credentialProvider = credentialProvider.threadSafe()
    }

    override suspend fun persist(monitoringId: String, arn: String) {
        credentialProvider.initializeRepositories(tableItemRepository)
        val item = monitoringTableBuilder.createResourceItem(monitoringId, arn)
        val entity = tableItemRepository.getItem(item.tableName,  item.key)
        if (!entity.any()) {
            tableItemRepository.add(item)
        }
    }
}