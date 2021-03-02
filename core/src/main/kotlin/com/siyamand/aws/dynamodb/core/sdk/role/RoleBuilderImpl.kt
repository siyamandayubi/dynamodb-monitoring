package com.siyamand.aws.dynamodb.core.sdk.role

import com.siyamand.aws.dynamodb.core.sdk.resource.ResourceType
import com.siyamand.aws.dynamodb.core.sdk.resource.TagEntity
import com.siyamand.aws.dynamodb.core.common.MonitorConfigProvider
import java.nio.file.Files
import java.nio.file.Paths

class RoleBuilderImpl(private val monitorConfigProvider: MonitorConfigProvider) : RoleBuilder {
    override fun createLambdaRole(): CreateRoleEntity {
        val uri = javaClass.classLoader.getResource("policies/LambdaAssumeRolePolicy.json").toURI()
        val assumePolicyDocument = Files.readString(Paths.get(uri))
        return CreateRoleEntityImpl(monitorConfigProvider.getLambdaRole(),
                assumePolicyDocument,
                null,
                null,
                null,
                mutableListOf(
                        TagEntity(monitorConfigProvider.getMonitoringMetadataIdTagName(), ResourceType.ROLE.value),
                        TagEntity(monitorConfigProvider.getMonitoringVersionTagName(), monitorConfigProvider.getMonitoringVersionValue())))
    }

    override fun createRdsProxyRole(): CreateRoleEntity {
        val uri = javaClass.classLoader.getResource("policies/RdsProxyAssumeRolePolicy.json").toURI()
        val assumePolicyDocument = Files.readString(Paths.get(uri))
        return CreateRoleEntityImpl(monitorConfigProvider.getRdsProxyRole(),
                assumePolicyDocument,
                null,
                null,
                null,
                mutableListOf(
                        TagEntity(monitorConfigProvider.getMonitoringMetadataIdTagName(), ResourceType.ROLE.value),
                        TagEntity(monitorConfigProvider.getMonitoringVersionTagName(), monitorConfigProvider.getMonitoringVersionValue())))
    }

    private class CreateRoleEntityImpl(
            override val roleName: String,
            override val assumeRolePolicyDocument: String? = null,
            override val description: String? = null,
            override val maxSessionDuration: Int? = null,
            override val permissionsBoundary: String? = null,
            override val tags: MutableList<TagEntity> = mutableListOf()) : CreateRoleEntity
}