package com.siyamand.aws.dynamodb.web.config

import com.siyamand.aws.dynamodb.web.filters.JwtRequestFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter

@Configuration
open class SecurityConfiguration {
    @Bean
    open fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        val jwtRequestFilter = JwtRequestFilter()
        return http.authorizeExchange()
                .pathMatchers("/**")
                .permitAll()
                .and()
                //.addFilterAt(jwtRequestFilter, SecurityWebFiltersOrder.AUTHENTICATION)
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