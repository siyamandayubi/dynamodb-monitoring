package com.siyamand.aws.dynamodb.web.controllers

import com.siyamand.aws.dynamodb.core.sdk.appconfig.AppConfigService
import com.siyamand.aws.dynamodb.web.models.CreateAppConfigModel
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ApplicationConfigController(private val appConfigService: AppConfigService) {

    @PostMapping("/api/appConfig/Create")
    suspend fun createApplication(@RequestBody model: CreateAppConfigModel): HttpEntity<String> {
        appConfigService.createConfig(model.applicationName, model.environment, model.strategyName, model.profileName, model.content)
        return ResponseEntity("OK", HttpStatus.OK)
    }
}