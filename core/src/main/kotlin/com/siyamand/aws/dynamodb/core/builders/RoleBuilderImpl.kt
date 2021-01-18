package com.siyamand.aws.dynamodb.core.builders

import com.siyamand.aws.dynamodb.core.entities.CreateRoleEntity
import com.siyamand.aws.dynamodb.core.entities.ResourceType
import com.siyamand.aws.dynamodb.core.entities.TagEntity
import com.siyamand.aws.dynamodb.core.services.MonitorConfigProvider
import java.nio.file.Files
import java.nio.file.Paths

class RoleBuilderImpl(private val monitorConfigProvider: MonitorConfigProvider) : RoleBuilder {
    override fun createLambdaRole(): CreateRoleEntity {
        val uri = javaClass.classLoader.getResource("policies/LambdaAssumeRolePolicy.json").toURI()
        val assumePolicyDocument = Files.readString(Paths.get(uri))
        return CreateRoleEntityImpl("Dynamodb-Monitoring-DB-Role",
        assumePolicyDocument,
        null,
        null,
        null,
        mutableListOf(TagEntity(monitorConfigProvider.getMonitoringGeneralTagName(), ResourceType.ROLE.value)))
    }

    private class CreateRoleEntityImpl(
            override val roleName: String,
            override val assumeRolePolicyDocument: String? = null,
            override val description: String? = null,
            override val maxSessionDuration: Int? = null,
            override val permissionsBoundary: String? = null,
            override val tags: MutableList<TagEntity> = mutableListOf()) : CreateRoleEntity
}