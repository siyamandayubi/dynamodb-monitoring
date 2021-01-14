package com.siyamand.aws.dynamodb.web.controllers

import com.siyamand.aws.dynamodb.core.entities.FunctionDetailEntity
import com.siyamand.aws.dynamodb.core.entities.FunctionEntity
import com.siyamand.aws.dynamodb.core.services.FunctionService
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class FunctionController(private val functionService: FunctionService) {
    @GetMapping("/api/functions")
    suspend fun getFunctions(): HttpEntity<List<FunctionEntity>> {
        return ResponseEntity(functionService.getFunctions(), HttpStatus.OK)
    }
    @GetMapping("/api/functions/{name}")
    suspend fun getFunctions(@PathVariable("name")name :String): HttpEntity<FunctionDetailEntity> {
        return ResponseEntity(functionService.getDetail(name), HttpStatus.OK)
    }}