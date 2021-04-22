package com.siyamand.aws.dynamodb.web.services

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import java.time.ZonedDateTime
import java.util.*

interface JwtSignerService {
    //https://medium.com/@jaidenashmore/jwt-authentication-in-spring-boot-webflux-6880c96247c7
    fun createJwt(userId: String, secretKey: String, sessionToken: String, date: ZonedDateTime): String

    /**
     * Validate the JWT where it will throw an exception if it isn't valid.
     */
    fun validateJwt(jwt: String): Jws<Claims>
}