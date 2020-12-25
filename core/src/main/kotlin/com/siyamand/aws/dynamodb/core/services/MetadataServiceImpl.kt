package com.siyamand.aws.dynamodb.core.services

import com.siyamand.aws.dynamodb.core.builders.MetadataTableBuilder
import com.siyamand.aws.dynamodb.core.entities.TableAttribute
import com.siyamand.aws.dynamodb.core.entities.TableDetailEntity
import com.siyamand.aws.dynamodb.core.entities.TableEntity
import com.siyamand.aws.dynamodb.core.entities.TableKeyScheme
import com.siyamand.aws.dynamodb.core.entities.monitoring.MonitoringMetadataEntity
import com.siyamand.aws.dynamodb.core.repositories.TableRepository

class MetadataServiceImpl(
        private  val metadataTableBuilder: MetadataTableBuilder,
        private val monitorConfigProvider: MonitorConfigProvider,
        private val tableRepository: TableRepository) : MetadataService {

    private var monitoringMetadataEntity: MonitoringMetadataEntity? = null
    override suspend fun load(): MonitoringMetadataEntity {
        TODO("Not yet implemented")
    }

    override suspend fun create() {
        val tableName = monitorConfigProvider.getMonitoringConfigMetadataTable()
        var detail = tableRepository.getDetail(tableName)
        if (detail == null) {
            tableRepository.add(metadataTableBuilder.build(monitorConfigProvider))
            detail = tableRepository.getDetail(tableName)
        }
    }

}