package com.siyamand.aws.dynamodb.core.sdk.secretManager

import com.siyamand.aws.dynamodb.core.sdk.resource.TagEntity
import com.siyamand.aws.dynamodb.core.database.DatabaseCredentialEntity
import com.siyamand.aws.dynamodb.core.common.MonitorConfigProvider
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SecretBuilderImpl(private val monitorConfigProvider: MonitorConfigProvider) : SecretBuilder {
    override fun buildCreateRequest(name: String, obj: DatabaseCredentialEntity, metadataId: String): CreateSecretEntity {
        return CreateSecretEntity(
                name,
                "",
                Json.encodeToString(obj),
                mutableListOf(
                        TagEntity(
                                monitorConfigProvider.getMonitoringMetadataIdTagName(),
                                metadataId),
                        TagEntity(monitorConfigProvider.getMonitoringVersionTagName(),
                                monitorConfigProvider.getMonitoringVersionValue())))
    }
}