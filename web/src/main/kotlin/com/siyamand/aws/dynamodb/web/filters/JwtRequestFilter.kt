package com.siyamand.aws.dynamodb.web.filters

import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono


class JwtRequestFilter : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        TODO("Not yet implemented")
    }
}