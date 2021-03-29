package com.siyamand.aws.dynamodb.infrastructure.mappers

import com.siyamand.aws.dynamodb.core.sdk.rds.entities.*
import com.siyamand.aws.dynamodb.core.sdk.resource.TagEntity
import software.amazon.awssdk.services.rds.model.*

class RdsMapper {
    companion object {
        fun convert(dbProxyTargetGroup: DBProxyTargetGroup): DbProxyTargetGroupEntity {
            return DbProxyTargetGroupEntity(
                    dbProxyTargetGroup.targetGroupName(),
                    dbProxyTargetGroup.isDefault ?: true,
                    ResourceMapper.convert(dbProxyTargetGroup.targetGroupArn()))
        }

        fun convert(entity: CreateProxyEntity): CreateDbProxyRequest {
            val builder = CreateDbProxyRequest
                    .builder()
                    .dbProxyName(entity.dbProxyName)
                    .roleArn(entity.roleArn)
                    .tags(entity.tags.map { Tag.builder().key(it.name).value(it.value).build() })
                    .engineFamily(entity.engineFamily)
                    .vpcSecurityGroupIds(entity.vpcSecurityGroupIds)
                    .vpcSubnetIds(entity.vpcSubnetIds)
                    .auth(entity.auth.map(RdsMapper::convert))

            return builder.build()
        }

        fun convert(proxy: DBProxy): RdsProxyEntity {
            val entity = RdsProxyEntity(
                    proxy.dbProxyName() ?: "",
                    ResourceMapper.convert(proxy.dbProxyArn()),
                    proxy.statusAsString() ?: "",
                    proxy.engineFamily() ?: "",
                    proxy.roleArn() ?: "",
                    proxy.endpoint() ?: "",
                    proxy.requireTLS(),
                    proxy.idleClientTimeout(),
                    proxy.createdDate(),
                    proxy.updatedDate()
            )

            entity.auth.addAll(proxy.auth().map {
                val auth = UserAuthConfigEntity()
                auth.description = it.description() ?: ""
                auth.authScheme = it.authSchemeAsString() ?: ""
                auth.secretArn = it.secretArn() ?: ""
                auth.iamAuth = it.iamAuthAsString() ?: ""
                auth
            })

            entity.vpcSecurityGroupIds.addAll(proxy.vpcSecurityGroupIds())
            entity.vpcSubnetIds.addAll(proxy.vpcSubnetIds())

            return entity
        }

        fun convert(entity: CreateDbProxyTargetEntity): RegisterDbProxyTargetsRequest {
            return RegisterDbProxyTargetsRequest
                    .builder()
                    .dbProxyName(entity.dbProxyName)
                    .targetGroupName(entity.targetGroupName)
                    .dbInstanceIdentifiers(entity.dbInstanceIdentifiers)
                    .build()
        }

        fun convert(target: DBProxyTarget): DbProxyTargetEntity {
            return DbProxyTargetEntity(
                    if (target.targetArn().isNullOrEmpty()) null else ResourceMapper.convert(target.targetArn()),
                    target.endpoint(),
                    target.trackedClusterId(),
                    target.rdsResourceId(),
                    target.port(),
                    target.type().name
            )
        }

        fun convert(dbInstance: DBInstance): RdsEntity {
            return RdsEntity(
                    dbInstance.dbInstanceIdentifier() ?: "",
                    dbInstance.endpoint()?.address() ?: "",
                    dbInstance.endpoint()?.port() ?: 0,
                    dbInstance.masterUsername(),
                    dbInstance.dbInstanceStatus(),
                    ResourceMapper.convert(dbInstance.dbInstanceArn()),
                    dbInstance.vpcSecurityGroups().map {
                        VpcSecurityGroupMembershipEntity(it.vpcSecurityGroupId(), it.status())
                    }.toMutableList(),
                    dbInstance.tagList().map { TagEntity(it.key(), it.value()) }.toMutableList()
            )
        }

        fun convert(entity: UserAuthConfigEntity): UserAuthConfig {
            val builder = UserAuthConfig
                    .builder()
                    .authScheme(entity.authScheme)
                    .description(entity.description)
                    .iamAuth(entity.iamAuth)
                    .secretArn(entity.secretArn)
                    .userName(entity.userName)

            return builder.build()
        }

        fun convert(entity: CreateDbInstanceEntity): CreateDbInstanceRequest {
            val builder = CreateDbInstanceRequest
                    .builder()
                    .dbName(entity.dbName)
                    .dbInstanceIdentifier(entity.instanceName)
                    .engine(entity.engine)
                    .engineVersion(entity.engineVersion)
                    .masterUsername(entity.masterUsername)
                    .dbInstanceClass(entity.dbInstanceClass)
                    .publiclyAccessible(entity.publiclyAccessible)
                    .masterUserPassword(entity.masterPassword)
                    .allocatedStorage(entity.allocatedStorage)
                    .tags(entity.tags.map { Tag.builder().key(it.name).value(it.value).build() })

            if (entity.vpcSecurityGroupIds.any()) {
                builder.vpcSecurityGroupIds(entity.vpcSecurityGroupIds)
            }

            if (!entity.availabilityZone.isEmpty()) {
                builder.availabilityZone(entity.availabilityZone)
            }

            return builder.build()
        }
    }
}