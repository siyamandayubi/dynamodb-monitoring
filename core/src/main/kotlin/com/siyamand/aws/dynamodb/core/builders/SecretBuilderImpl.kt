package com.siyamand.aws.dynamodb.core.builders

import com.siyamand.aws.dynamodb.core.entities.CreateSecretEntity
import com.siyamand.aws.dynamodb.core.entities.ResourceType
import com.siyamand.aws.dynamodb.core.entities.TagEntity
import com.siyamand.aws.dynamodb.core.entities.database.DatabaseCredentialEntity
import com.siyamand.aws.dynamodb.core.services.MonitorConfigProvider
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SecretBuilderImpl(private val monitorConfigProvider: MonitorConfigProvider): SecretBuilder {
    override fun buildCreateRequest(name: String, obj: DatabaseCredentialEntity): CreateSecretEntity {
        return  CreateSecretEntity(
                name,
                "",
                Json.encodeToString(obj),
                mutableListOf(TagEntity(monitorConfigProvider.getMonitoringGeneralTagName(), ResourceType.SECRET_MANAGER.value)))
    }
}