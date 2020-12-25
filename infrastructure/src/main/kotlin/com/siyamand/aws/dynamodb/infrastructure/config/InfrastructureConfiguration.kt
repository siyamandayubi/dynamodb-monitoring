package com.siyamand.aws.dynamodb.infrastructure.config

import com.siyamand.aws.dynamodb.core.repositories.*
import com.siyamand.aws.dynamodb.core.services.CredentialProvider
import com.siyamand.aws.dynamodb.core.services.MonitorConfigProvider
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilder
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilderImpl
import com.siyamand.aws.dynamodb.infrastructure.repositories.*
import com.siyamand.aws.dynamodb.infrastructure.services.CredentialProviderImpl
import com.siyamand.aws.dynamodb.infrastructure.services.StaticMonitorConfigProviderImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan
open class InfrastructureConfiguration() {
    @Bean
    open fun getTableItemRepository(clientBuilder: ClientBuilder):TableItemRepository{
        return TableItemRepositoryImpl(clientBuilder)
    }

    @Bean
    open fun getTableRepository(clientBuilder: ClientBuilder): TableRepository {
        return TableRepositoryImpl(clientBuilder)
    }

    @Bean
    open fun getCredentialProvider(): CredentialProvider {
        return CredentialProviderImpl()
    }

    @Bean
    open fun getMonitoringConfigProvider(): MonitorConfigProvider{
        return StaticMonitorConfigProviderImpl()
    }

    @Bean
    internal open fun getClientBuilder(): ClientBuilder {
        return ClientBuilderImpl()
    }

    @Bean
    open fun getLambdaRepository(clientBuilder: ClientBuilder): LambdaRepository{
        return LambdaRepositoryImpl(clientBuilder)
    }

    @Bean
    open fun getTokenRepository(clientBuilder: ClientBuilder): TokenRepository{
        return TokenRepositoryImpl(clientBuilder)
    }

    @Bean
    open fun getRoleRepository(clientBuilder: ClientBuilder): RoleRepository{
        return RoleRepositoryImpl(clientBuilder)
    }
}