package com.siyamand.aws.dynamodb.core.config

import com.siyamand.aws.dynamodb.core.repositories.*
import com.siyamand.aws.dynamodb.core.services.*
import com.siyamand.aws.dynamodb.core.services.TableServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan
open class CoreConfiguration {

    @Bean
    open fun getMonitoringTableAggregate(
            monitorConfigProvider: MonitorConfigProvider,
            tableRepository: TableRepository,
            resourceRepository: ResourceRepository): MetadataService {
        return MetadataServiceImpl(resourceRepository, monitorConfigProvider, tableRepository)
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
    open fun getFunctionService(lambdaRepository: LambdaRepository, credentialProvider: CredentialProvider): FunctionService {
        return FunctionServiceImpl(lambdaRepository, credentialProvider)
    }

    @Bean
    open fun getRoleService(roleRepository: RoleRepository, credentialProvider: CredentialProvider): RoleService {
        return RoleServiceImpl(roleRepository, credentialProvider)
    }
}