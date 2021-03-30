package com.siyamand.aws.dynamodb.web.config

import com.siyamand.aws.dynamodb.web.services.JwtSignerService
import com.siyamand.aws.dynamodb.web.services.JwtSignerServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter

@Configuration
open class SecurityConfiguration {
    @Bean
    open fun getJwtSigner(environment: Environment): JwtSignerService {
        return JwtSignerServiceImpl(environment)
    }

    @Bean
    open fun securityWebFilterChain(http: ServerHttpSecurity,
                                    jwtAuthenticationManager: ReactiveAuthenticationManager,
                                    jwtAuthenticationConverter: ServerAuthenticationConverter): SecurityWebFilterChain {
        val authenticationWebFilter = AuthenticationWebFilter(jwtAuthenticationManager)
        authenticationWebFilter.setServerAuthenticationConverter(jwtAuthenticationConverter)

        return http.cors().and().authorizeExchange()
                .pathMatchers("/login")
                .permitAll()
                .pathMatchers("/api/**")
                .authenticated()
                .pathMatchers("/**")
                .permitAll()
                .and()
                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .httpBasic()
                .disable()
                .csrf()
                .disable()
                .formLogin()
                .disable()
                .logout()
                .disable()
                .build()
    }
}