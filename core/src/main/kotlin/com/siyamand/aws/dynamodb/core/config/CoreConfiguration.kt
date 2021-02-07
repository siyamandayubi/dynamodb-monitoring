package com.siyamand.aws.dynamodb.core.config

import com.siyamand.aws.dynamodb.core.authentication.AuthenticationService
import com.siyamand.aws.dynamodb.core.authentication.AuthenticationServiceImpl
import com.siyamand.aws.dynamodb.core.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.authentication.TokenRepository
import com.siyamand.aws.dynamodb.core.common.MonitorConfigProvider
import com.siyamand.aws.dynamodb.core.database.*
import com.siyamand.aws.dynamodb.core.dynamodb.TableItemRepository
import com.siyamand.aws.dynamodb.core.lambda.FunctionBuilder
import com.siyamand.aws.dynamodb.core.lambda.FunctionBuilderImpl
import com.siyamand.aws.dynamodb.core.lambda.LambdaRepository
import com.siyamand.aws.dynamodb.core.lambda.FunctionService
import com.siyamand.aws.dynamodb.core.lambda.FunctionServiceImpl
import com.siyamand.aws.dynamodb.core.network.VpcRepository
import com.siyamand.aws.dynamodb.core.network.VpcService
import com.siyamand.aws.dynamodb.core.network.VpcServiceImpl
import com.siyamand.aws.dynamodb.core.rds.*
import com.siyamand.aws.dynamodb.core.resource.ResourceRepository
import com.siyamand.aws.dynamodb.core.role.*
import com.siyamand.aws.dynamodb.core.s3.S3Repository
import com.siyamand.aws.dynamodb.core.s3.S3Service
import com.siyamand.aws.dynamodb.core.s3.S3ServiceImpl
import com.siyamand.aws.dynamodb.core.secretManager.SecretBuilder
import com.siyamand.aws.dynamodb.core.secretManager.SecretBuilderImpl
import com.siyamand.aws.dynamodb.core.secretManager.SecretManagerRepository
import com.siyamand.aws.dynamodb.core.monitoring.*
import com.siyamand.aws.dynamodb.core.dynamodb.TableRepository
import com.siyamand.aws.dynamodb.core.dynamodb.TableServiceImpl
import com.siyamand.aws.dynamodb.core.dynamodb.TableService
import com.siyamand.aws.dynamodb.core.workflow.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan
open class CoreConfiguration {

    @Bean
    open fun getMonitoringItemBuilder(): MonitoringItemBuilder {
        return MonitoringItemBuilderImpl()
    }

    @Bean
    open fun getWorkflowBuilder(templates: Iterable<WorkflowTemplate>): WorkflowBuilder {
        return WorkflowBuilderImpl(templates)
    }

    @Bean
    open fun getWorkflowPersister(
            tableItemRepository: TableItemRepository,
            monitoringItemBuilder: MonitoringItemBuilder,
            workflowBuilder: WorkflowBuilder,
            monitorConfigProvider: MonitorConfigProvider): WorkflowPersister {
        return WorkflowPersisterImpl(tableItemRepository, monitoringItemBuilder,workflowBuilder, monitorConfigProvider)
    }

    @Bean
    open fun getWorkflowManager(workflowPersistance: WorkflowPersister): WorkflowManager {
        return WorkflowManagerImpl(workflowPersistance)
    }

    @Bean
    open fun getVpcService(credentialProvider: CredentialProvider, vpcRepository: VpcRepository): VpcService {
        return VpcServiceImpl(credentialProvider, vpcRepository)
    }

    @Bean
    open fun getMonitoringTableAggregate(
            monitorConfigProvider: MonitorConfigProvider,
            tableRepository: TableRepository,
            resourceRepository: ResourceRepository): MetadataService {
        return MetadataServiceImpl(resourceRepository, monitorConfigProvider, tableRepository)
    }

    @Bean
    open fun getS3Service(credentialProvider: CredentialProvider, monitorConfigProvider: MonitorConfigProvider, s3Repository: S3Repository): S3Service {
        return S3ServiceImpl(credentialProvider, s3Repository, monitorConfigProvider)
    }

    @Bean
    open fun getRdsService(
            roleService: RoleService,
            credentialProvider: CredentialProvider,
            rdsRepository: RdsRepository,
            vpcRepository: VpcRepository,
            secretBuilder: SecretBuilder,
            secretManagerRepository: SecretManagerRepository,
            databaseRepository: DatabaseRepository,
            resourceRepository: ResourceRepository,
            rdsBuilder: RdsBuilder,
            databaseCredentialBuilder: DatabaseCredentialBuilder): RdsService {
        return RdsServiceImpl(
                roleService,
                credentialProvider,
                rdsBuilder,
                secretBuilder,
                databaseCredentialBuilder,
                rdsRepository,
                resourceRepository,
                vpcRepository,
                databaseRepository,
                secretManagerRepository)
    }

    @Bean
    open fun getDatabaseService(monitorConfigProvider: MonitorConfigProvider, databaseRepository: DatabaseRepository): DatabaseService {
        return DatabaseServiceImpl(monitorConfigProvider, databaseRepository)
    }

    @Bean
    open fun getRdsBuilder(monitorConfigProvider: MonitorConfigProvider): RdsBuilder {
        return RdsBuilderImpl(monitorConfigProvider)
    }

    @Bean
    open fun getSecretBuilder(monitorConfigProvider: MonitorConfigProvider): SecretBuilder {
        return SecretBuilderImpl(monitorConfigProvider)
    }

    @Bean
    open fun getDatabaseCredentialBuilder(): DatabaseCredentialBuilder {
        return DatabaseCredentialBuilderImpl()
    }

    @Bean
    open fun getTableService(
            tableRepository: TableRepository,
            credentialProvider: CredentialProvider): TableService {
        return TableServiceImpl(tableRepository, credentialProvider)
    }

    @Bean
    open fun getAuthenticationService(tokenRepository: TokenRepository): AuthenticationService {
        return AuthenticationServiceImpl(tokenRepository)
    }

    @Bean
    open fun getFunctionBuilder(): FunctionBuilder {
        return FunctionBuilderImpl()
    }

    @Bean
    open fun getFunctionService(
            monitorConfigProvider: MonitorConfigProvider,
            roleService: RoleService,
            functionBuilder: FunctionBuilder,
            lambdaRepository: LambdaRepository,
            credentialProvider: CredentialProvider): FunctionService {
        return FunctionServiceImpl(monitorConfigProvider, functionBuilder, roleService, lambdaRepository, credentialProvider)
    }

    @Bean
    open fun getPolicyBuilder(): PolicyBuilder {
        return PolicyBuilderImpl()
    }

    @Bean
    open fun getRoleBuilder(monitorConfigProvider: MonitorConfigProvider): RoleBuilder {
        return RoleBuilderImpl(monitorConfigProvider)
    }

    @Bean
    open fun getRoleService(
            roleRepository: RoleRepository,
            resourceRepository: ResourceRepository,
            credentialProvider: CredentialProvider,
            monitorConfigProvider: MonitorConfigProvider,
            policyBuilder: PolicyBuilder,
            roleBuilder: RoleBuilder): RoleService {
        return RoleServiceImpl(roleRepository, resourceRepository, credentialProvider, monitorConfigProvider, policyBuilder, roleBuilder)
    }
}