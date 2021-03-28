package com.siyamand.aws.dynamodb.web.controllers

import com.siyamand.aws.dynamodb.core.sdk.rds.entities.RdsListEntity
import com.siyamand.aws.dynamodb.core.sdk.rds.RdsService
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RdsController(private val rdsService: RdsService) {

    @GetMapping("/api/rds/list")
    suspend fun getList(): HttpEntity<RdsListEntity> {
        val list = this.rdsService.getList("")
        return ResponseEntity(list, HttpStatus.OK)
    }
}