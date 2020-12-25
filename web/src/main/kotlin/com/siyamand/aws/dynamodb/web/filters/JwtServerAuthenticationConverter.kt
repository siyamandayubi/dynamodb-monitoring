package com.siyamand.aws.dynamodb.web.filters

import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class JwtServerAuthenticationConverter : ServerAuthenticationConverter {
    override fun convert(exchange: ServerWebExchange?): Mono<Authentication> {
        return Mono.justOrEmpty(exchange)
                .flatMap { Mono.justOrEmpty(it?.request?.headers?.get("X-Auth")) }
                .filter { it.isNotEmpty() }
                .map { it[0] }
                .map { PreAuthenticatedAuthenticationToken(it, it) }
    }
}