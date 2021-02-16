package com.siyamand.aws.dynamodb.core.secretManager

import com.siyamand.aws.dynamodb.core.resource.ResourceType
import com.siyamand.aws.dynamodb.core.resource.TagEntity
import com.siyamand.aws.dynamodb.core.database.DatabaseCredentialEntity
import com.siyamand.aws.dynamodb.core.common.MonitorConfigProvider
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SecretBuilderImpl(private val monitorConfigProvider: MonitorConfigProvider): SecretBuilder {
    override fun buildCreateRequest(name: String, obj: DatabaseCredentialEntity, metadataId: String): CreateSecretEntity {
        return  CreateSecretEntity(
                name,
                "",
                Json.encodeToString(obj),
                mutableListOf(
                        TagEntity(
                                monitorConfigProvider.getMonitoringMetadataIdTagName(),
                                metadataId)))
    }
}