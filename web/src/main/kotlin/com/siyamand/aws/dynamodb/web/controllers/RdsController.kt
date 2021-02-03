package com.siyamand.aws.dynamodb.web.controllers

import com.siyamand.aws.dynamodb.core.rds.RdsListEntity
import com.siyamand.aws.dynamodb.core.resource.ResourceEntity
import com.siyamand.aws.dynamodb.core.rds.RdsService
import com.siyamand.aws.dynamodb.web.models.CreateRdsModel
import com.siyamand.aws.dynamodb.web.models.CreateRdsProxyModel
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class RdsController(private val rdsService: RdsService) {

    @PostMapping("/api/rds/create-rds")
    suspend fun createDbInstance(@RequestBody model: CreateRdsModel): HttpEntity<ResourceEntity> {
        val rds = this.rdsService.createDbInstance(model.name)
        return ResponseEntity(rds, HttpStatus.OK)
    }

    @PostMapping("/api/rds/create-proxy")
    suspend fun createProxy(@RequestBody model: CreateRdsProxyModel): HttpEntity<ResourceEntity> {
        val rds = this.rdsService.createProxy(model.name, model.secretId)
        return ResponseEntity(rds, HttpStatus.OK)
    }
    @PostMapping("/api/rds/create-database")
    suspend fun createDatabase(@RequestBody model: CreateRdsProxyModel): HttpEntity<String> {
        this.rdsService.createDatabase(model.name, model.secretId)
        return ResponseEntity("Finished", HttpStatus.OK)
    }

    @GetMapping("/api/rds/list")
    suspend fun getList(): HttpEntity<RdsListEntity> {
        val list = this.rdsService.getList("")
        return ResponseEntity(list, HttpStatus.OK)
    }
}