package com.siyamand.aws.dynamodb.web.controllers

import com.siyamand.aws.dynamodb.core.services.AuthenticationService
import org.springframework.http.HttpEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthenticateController(private val authenticationService: AuthenticationService) {
    @PostMapping("/login")
    fun login(): HttpEntity<Void>{
        TODO()
    }
}