package com.siyamand.aws.dynamodb.core.config

import com.siyamand.aws.dynamodb.core.sdk.authentication.AuthenticationService
import com.siyamand.aws.dynamodb.core.sdk.authentication.AuthenticationServiceImpl
import com.siyamand.aws.dynamodb.core.sdk.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.sdk.authentication.TokenRepository
import com.siyamand.aws.dynamodb.core.common.MonitorConfigProvider
import com.siyamand.aws.dynamodb.core.database.*
import com.siyamand.aws.dynamodb.core.sdk.dynamodb.*
import com.siyamand.aws.dynamodb.core.sdk.dynamodb.TableServiceImpl
import com.siyamand.aws.dynamodb.core.sdk.network.VpcRepository
import com.siyamand.aws.dynamodb.core.sdk.network.VpcService
import com.siyamand.aws.dynamodb.core.sdk.network.VpcServiceImpl
import com.siyamand.aws.dynamodb.core.sdk.rds.*
import com.siyamand.aws.dynamodb.core.sdk.resource.ResourceRepository
import com.siyamand.aws.dynamodb.core.sdk.role.*
import com.siyamand.aws.dynamodb.core.sdk.s3.S3Repository
import com.siyamand.aws.dynamodb.core.sdk.s3.S3Service
import com.siyamand.aws.dynamodb.core.sdk.s3.S3ServiceImpl
import com.siyamand.aws.dynamodb.core.sdk.secretManager.SecretBuilder
import com.siyamand.aws.dynamodb.core.sdk.secretManager.SecretBuilderImpl
import com.siyamand.aws.dynamodb.core.sdk.secretManager.SecretManagerRepository
import com.siyamand.aws.dynamodb.core.monitoring.*
import com.siyamand.aws.dynamodb.core.sdk.lambda.*
import com.siyamand.aws.dynamodb.core.schedule.WorkflowJobHandler
import com.siyamand.aws.dynamodb.core.schedule.WorkflowJobHandlerImpl
import com.siyamand.aws.dynamodb.core.sdk.appconfig.*
import com.siyamand.aws.dynamodb.core.sdk.secretManager.CreateSecretManagerWorkflowStep
import com.siyamand.aws.dynamodb.core.template.TemplateEngine
import com.siyamand.aws.dynamodb.core.template.TemplateEngineImpl
import com.siyamand.aws.dynamodb.core.workflow.*
import com.siyamand.aws.dynamodb.core.workflow.defaultSteps.AssignWorkflowStep
import com.siyamand.aws.dynamodb.core.workflow.defaultSteps.IfElseWorkflowStep
import com.siyamand.aws.dynamodb.core.workflow.defaultSteps.JumpWorkflowStep
import com.siyamand.aws.dynamodb.core.workflow.defaultSteps.RemoveVariableWorkflowStep
import com.siyamand.aws.dynamodb.core.workflow.templates.AggregateSimpleMysqlDatabaseTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.TaskScheduler

@Configuration
@ComponentScan
open class CoreConfiguration {
    @Bean
    open fun getMonitoringResourcePersister(
            credentialProvider: CredentialProvider,
            monitorConfigProvider: MonitorConfigProvider,
            monitoringTableBuilder: MonitoringTableBuilder,
            tableItemRepository: TableItemRepository): MonitoringResourcePersister {
        return MonitoringResourcePersisterImpl(credentialProvider, tableItemRepository, monitoringTableBuilder)
    }

    @Bean
    open fun getRemoveVariableWorkflowStep(): WorkflowStep {
        return RemoveVariableWorkflowStep()
    }

    @Bean
    open fun getRdsConfigBuilderWorkflowStep(
            credentialProvider: CredentialProvider,
            rdsRepository: RdsRepository,
            rdsConfigBuilder: RdsConfigBuilder): WorkflowStep {
        return RdsConfigBuilderWorkflowStep(credentialProvider, rdsRepository, rdsConfigBuilder)
    }

    @Bean
    open fun getAssignWorkflowStep(templateEngine: TemplateEngine): WorkflowStep {
        return AssignWorkflowStep(templateEngine)
    }

    @Bean
    open fun getIfElseWorkflowStep(templateEngine: TemplateEngine): WorkflowStep {
        return IfElseWorkflowStep(templateEngine)
    }

    @Bean
    open fun getJumpWorkflowStep(): WorkflowStep {
        return JumpWorkflowStep()
    }

    @Bean
    open fun getCreateAppConfigWorkflowStep(credentialProvider: CredentialProvider,
                                            appConfigRepository: AppConfigRepository,
                                            s3Service: S3Service,
                                            roleService: RoleService,
                                            appConfigBuilder: AppConfigBuilder): WorkflowStep {

        return CreateAppConfigWorkflowStep(credentialProvider, appConfigRepository, s3Service, roleService, appConfigBuilder)
    }

    @Bean
    open fun getRdsConfigBuilder(): RdsConfigBuilder {
        return RdsConfigBuilderImpl()
    }

    @Bean
    open fun getAppConfigBuilder(monitorConfigProvider: MonitorConfigProvider): AppConfigBuilder {
        return AppConfigBuilderImpl(monitorConfigProvider)
    }

    @Bean
    open fun getAppConfigService(appConfigRepository: AppConfigRepository,
                                 credentialProvider: CredentialProvider,
                                 roleService: RoleService,
                                 s3Service: S3Service,
                                 appConfigBuilder: AppConfigBuilder): AppConfigService {
        return AppConfigServiceImpl(appConfigRepository, credentialProvider, s3Service, roleService, appConfigBuilder)
    }

    @Bean
    open fun getEnableDynamoDbStreamWorkflowStep(
            credentialProvider: CredentialProvider,
            resourceRepository: ResourceRepository,
            tableRepository: TableRepository): WorkflowStep {
        return EnableDynamoDbStreamWorkflowStep(credentialProvider, resourceRepository, tableRepository)
    }

    @Bean
    open fun getMetadataService(tableItemRepository: TableItemRepository,
                                credentialProvider: CredentialProvider,
                                monitorConfigProvider: MonitorConfigProvider,
                                monitoringTableBuilder: MonitoringTableBuilder,
                                monitoringItemConverter: MonitoringItemConverter): MetadataService {
        return MetadataServiceImpl(tableItemRepository, credentialProvider, monitoringTableBuilder, monitorConfigProvider, monitoringItemConverter)
    }

    @Bean
    open fun getPrerequisiteService(
            monitorConfigProvider: MonitorConfigProvider,
            roleRepository: RoleRepository,
            roleService: RoleService,
            credentialProvider: CredentialProvider,
            tableRepository: TableRepository,
            monitoringTableBuilder: MonitoringTableBuilder): PrerequisiteService {
        return PrerequisiteServiceImpl(monitorConfigProvider, credentialProvider, roleRepository, roleService, tableRepository, monitoringTableBuilder)
    }

    @Bean
    open fun getAddLambdaEventSourceWorkflowStep(credentialProvider: CredentialProvider,
                                                 functionBuilder: FunctionBuilder,
                                                 lambdaRepository: LambdaRepository,
                                                 monitoringResourcePersister: MonitoringResourcePersister): WorkflowStep {
        return AddLambdaEventSourceWorkflowStep(credentialProvider, functionBuilder, lambdaRepository, monitoringResourcePersister)
    }

    @Bean
    open fun getAddLambdaFunctionWorkflowStep(
            credentialProvider: CredentialProvider,
            lambdaRepository: LambdaRepository,
            rdsRepository: RdsRepository,
            secretManagerRepository: SecretManagerRepository,
            roleRepository: RoleRepository,
            vpcRepository: VpcRepository,
            functionBuilder: FunctionBuilder,
            monitoringResourcePersister: MonitoringResourcePersister): WorkflowStep {
        return AddLambdaFunctionWorkflowStep(
                credentialProvider,
                lambdaRepository,
                roleRepository,
                rdsRepository,
                secretManagerRepository,
                vpcRepository,
                functionBuilder,
                monitoringResourcePersister)
    }

    @Bean
    open fun getAggregateMonitoringEntityCodeGeneratorStep(templateEngine: TemplateEngine): WorkflowStep {
        return AggregateMonitoringEntityCodeGeneratorStep(templateEngine)
    }

    @Bean
    open fun getTemplateEngine(): TemplateEngine {
        return TemplateEngineImpl()
    }

    @Bean
    open fun getCreateRdsProxyTargetGroupWorkflowStep(credentialProvider: CredentialProvider,
                                                      rdsRepository: RdsRepository,
                                                      resourceRepository: ResourceRepository,
                                                      monitoringResourcePersister: MonitoringResourcePersister): WorkflowStep {
        return CreateRdsProxyTargetGroupWorkflowStep(credentialProvider, rdsRepository, resourceRepository, monitoringResourcePersister)
    }

    @Bean
    open fun getCreateRdsProxyWorkflowStep(roleRepository: RoleRepository,
                                           monitorConfigProvider: MonitorConfigProvider,
                                           credentialProvider: CredentialProvider,
                                           rdsRepository: RdsRepository,
                                           vpcRepository: VpcRepository,
                                           rdsBuilder: RdsBuilder,
                                           resourceRepository: ResourceRepository,
                                           monitoringResourcePersister: MonitoringResourcePersister): WorkflowStep {
        return CreateRdsProxyWorkflowStep(roleRepository,
                monitorConfigProvider,
                credentialProvider,
                rdsRepository,
                vpcRepository,
                rdsBuilder,
                resourceRepository,
                monitoringResourcePersister)
    }

    @Bean
    open fun getAddLambdaLayerWorkflowStep(monitorConfigProvider: MonitorConfigProvider,
                                           credentialProvider: CredentialProvider,
                                           functionBuilder: FunctionBuilder,
                                           lambdaRepository: LambdaRepository,
                                           monitoringResourcePersister: MonitoringResourcePersister): WorkflowStep {
        return AddLambdaLayerWorkflowStep(credentialProvider, lambdaRepository, functionBuilder, monitoringResourcePersister)
    }

    @Bean
    open fun getWorkflowJobHandler(monitorConfigProvider: MonitorConfigProvider,
                                   s3Service: S3Service,
                                   monitoringItemConverter: MonitoringItemConverter,
                                   workflowConverter: WorkflowConverter,
                                   workflowManager: WorkflowManager,
                                   workflowPersister: WorkflowPersister,
                                   tableItemRepository: TableItemRepository): WorkflowJobHandler {
        return WorkflowJobHandlerImpl(monitorConfigProvider, s3Service, monitoringItemConverter, workflowConverter, workflowManager, workflowPersister, tableItemRepository)
    }

    @Bean
    open fun getCreateDatabaseWorkflowStep(credentialProvider: CredentialProvider,
                                           databaseRepository: DatabaseRepository,
                                           resourceRepository: ResourceRepository,
                                           rdsRepository: RdsRepository,
                                           secretManagerRepository: SecretManagerRepository): WorkflowStep {
        return CreateDatabaseWorkflowStep(credentialProvider, databaseRepository, resourceRepository, rdsRepository, secretManagerRepository)
    }

    @Bean
    open fun getExecuteStatementDatabaseWorkflowStep(credentialProvider: CredentialProvider,
                                                     databaseRepository: DatabaseRepository,
                                                     resourceRepository: ResourceRepository,
                                                     rdsRepository: RdsRepository,
                                                     secretManagerRepository: SecretManagerRepository): WorkflowStep {
        return ExecuteStatementDatabaseWorkflowStep(credentialProvider, databaseRepository, resourceRepository, rdsRepository, secretManagerRepository)
    }

    @Bean
    open fun getCreateDatabaseTableWorkflowStep(credentialProvider: CredentialProvider,
                                                databaseRepository: DatabaseRepository,
                                                resourceRepository: ResourceRepository,
                                                rdsRepository: RdsRepository,
                                                secretManagerRepository: SecretManagerRepository,
                                                templateEngine: TemplateEngine): WorkflowStep {
        return CreateDatabaseTableWorkflowStep(credentialProvider, databaseRepository, resourceRepository, rdsRepository, secretManagerRepository, templateEngine)
    }

    @Bean
    open fun getCreateRdsInstanceWorkflowStep(
            monitorConfigProvider: MonitorConfigProvider,
            rdsRepository: RdsRepository,
            secretManagerRepository: SecretManagerRepository,
            credentialProvider: CredentialProvider,
            resourceRepository: ResourceRepository,
            rdsBuilder: RdsBuilder,
            monitoringResourcePersister: MonitoringResourcePersister): WorkflowStep {
        return CreateRdsInstanceWorkflowStep(monitorConfigProvider, rdsRepository, secretManagerRepository, credentialProvider, resourceRepository, rdsBuilder, monitoringResourcePersister)
    }

    @Bean
    open fun getCreateSecretManagerWorkflowStep(credentialProvider: CredentialProvider,
                                                secretBuilder: SecretBuilder,
                                                databaseCredentialBuilder: DatabaseCredentialBuilder,
                                                secretManagerRepository: SecretManagerRepository,
                                                monitoringResourcePersister: MonitoringResourcePersister): WorkflowStep {
        return CreateSecretManagerWorkflowStep(credentialProvider, secretBuilder, databaseCredentialBuilder, secretManagerRepository, monitoringResourcePersister)
    }

    @Bean
    open fun getMonitoringTableBuilder(configProvider: MonitorConfigProvider): MonitoringTableBuilder {
        return MonitoringTableBuilderImpl(configProvider)
    }

    @Bean
    open fun getWorkflowTemplates(
            monitorConfigProvider: MonitorConfigProvider,
            credentialProvider: CredentialProvider, allSteps: List<WorkflowStep>): WorkflowTemplate {
        return AggregateSimpleMysqlDatabaseTemplate(monitorConfigProvider, credentialProvider, allSteps)
    }

    @Bean
    open fun getMonitoringItemBuilder(resourceRepository: ResourceRepository): MonitoringItemConverter {
        return MonitoringItemConverterImpl(resourceRepository)
    }

    @Bean
    open fun getWorkflowConverter(templates: List<WorkflowTemplate>): WorkflowConverter {
        return WorkflowConverterImpl(templates)
    }

    @Bean
    open fun getWorkflowBuilder(templates: List<WorkflowTemplate>): WorkflowBuilder {
        return WorkflowBuilderImpl(templates)
    }

    @Bean
    open fun getWorkflowPersister(
            tableItemRepository: TableItemRepository,
            credentialProvider: CredentialProvider,
            s3Service: S3Service,
            monitoringItemBuilder: MonitoringItemConverter,
            workflowConverter: WorkflowConverter,
            monitorConfigProvider: MonitorConfigProvider): WorkflowPersister {
        return WorkflowPersisterImpl(tableItemRepository, credentialProvider, monitoringItemBuilder, workflowConverter, s3Service, monitorConfigProvider)
    }

    @Bean
    open fun getWorkflowManager(): WorkflowManager {
        return WorkflowManagerImpl()
    }

    @Bean
    open fun getVpcService(credentialProvider: CredentialProvider, vpcRepository: VpcRepository): VpcService {
        return VpcServiceImpl(credentialProvider, vpcRepository)
    }

    @Bean
    open fun getMonitoringService(
            s3Service: S3Service,
            prerequisiteReadonlyService: PrerequisiteReadonlyService,
            workflowConverter: WorkflowConverter,
            monitorConfigProvider: MonitorConfigProvider,
            tableRepository: TableRepository,
            credentialProvider: CredentialProvider,
            workflowManager: WorkflowManager,
            workflowPersister: WorkflowPersister,
            workflowBuilder: WorkflowBuilder,
            tableItemRepository: TableItemRepository,
            monitoringItemConverter: MonitoringItemConverter,
            scheduler: TaskScheduler): WorkflowService {
        return WorkflowServiceImpl(
                s3Service,
                prerequisiteReadonlyService,
                workflowConverter,
                monitorConfigProvider,
                credentialProvider,
                workflowBuilder,
                workflowManager,
                workflowPersister,
                tableItemRepository,
                monitoringItemConverter,
                tableRepository,
                scheduler)
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
    open fun getFunctionBuilder(monitorConfigProvider: MonitorConfigProvider): FunctionBuilder {
        return FunctionBuilderImpl(monitorConfigProvider)
    }

    @Bean
    open fun getFunctionService(
            monitorConfigProvider: MonitorConfigProvider,
            roleService: RoleService,
            functionBuilder: FunctionBuilder,
            lambdaRepository: LambdaRepository,
            credentialProvider: CredentialProvider): FunctionService {
        return FunctionServiceImpl(functionBuilder, roleService, lambdaRepository, credentialProvider)
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