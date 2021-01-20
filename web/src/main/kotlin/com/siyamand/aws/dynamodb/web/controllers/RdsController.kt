package com.siyamand.aws.dynamodb.web.controllers

import com.siyamand.aws.dynamodb.core.entities.ResourceEntity
import com.siyamand.aws.dynamodb.core.services.RdsService
import com.siyamand.aws.dynamodb.web.models.CreateRdsModel
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class RdsController(private val rdsService: RdsService) {
    @PostMapping("/api/rds/create-rds")
    suspend fun createLambdaRole(@RequestBody model: CreateRdsModel): HttpEntity<ResourceEntity> {
        val rds = this.rdsService.createDbInstance(model.name)
        return ResponseEntity(rds, HttpStatus.OK)
    }}