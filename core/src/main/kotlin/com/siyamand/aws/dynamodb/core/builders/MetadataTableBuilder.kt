package com.siyamand.aws.dynamodb.core.builders

import com.siyamand.aws.dynamodb.core.entities.TableDetailEntity
import com.siyamand.aws.dynamodb.core.services.MonitorConfigProvider

interface MetadataTableBuilder {
    fun build(monitorConfigProvider: MonitorConfigProvider):TableDetailEntity
}