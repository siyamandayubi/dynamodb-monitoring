package com.siyamand.aws.dynamodb.web

import com.siyamand.aws.dynamodb.core.sdk.authentication.TokenCredentialEntity
import com.siyamand.aws.dynamodb.web.services.JwtSignerService
import com.siyamand.aws.dynamodb.web.services.TokenClaims
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
// https://medium.com/@jaidenashmore/jwt-authentication-in-spring-boot-webflux-6880c96247c7
class JwtAuthenticationManager(private val jwtSignerService: JwtSignerService) : ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication?): Mono<Authentication> {
        return Mono.just(authentication)
                .map { jwtSignerService.validateJwt(it?.credentials as String) }
                .map { jws ->
                    PreAuthenticatedAuthenticationToken(
                            TokenCredentialEntity(
                                    jws.body[TokenClaims.ACCESS_KEY].toString(),
                                    jws.body[TokenClaims.SECRET_KEY].toString(),
                                    jws.body[TokenClaims.SESSION_TOKEN].toString(),
                                    null),
                            authentication?.credentials as String,
                            mutableListOf(SimpleGrantedAuthority("ROLE_USER"))
                    )
                }
    }
}