package com.siyamand.aws.dynamodb.web.controllers

import com.siyamand.aws.dynamodb.core.common.PageResultEntity
import com.siyamand.aws.dynamodb.core.sdk.lambda.*
import com.siyamand.aws.dynamodb.core.sdk.resource.ResourceEntity
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class FunctionController(private val functionService: FunctionService) {
    @GetMapping("/api/functions")
    suspend fun getFunctions(): HttpEntity<List<FunctionEntity>> {
        return ResponseEntity(functionService.getFunctions(), HttpStatus.OK)
    }

    @GetMapping("/api/functions/{name}")
    suspend fun getFunctions(@PathVariable("name") name: String): HttpEntity<FunctionDetailEntity?> {
        return ResponseEntity(functionService.getDetail(name), HttpStatus.OK)
    }

    @GetMapping("/api/layers/")
    suspend fun getLayers(@RequestAttribute("marker", required =  false) marker: String?): HttpEntity<PageResultEntity<FunctionLayerListEntity>> {
        return ResponseEntity(functionService.getLayers(marker ?: ""), HttpStatus.OK)
    }

    @GetMapping("/api/layers/{name}")
    suspend fun getLayer(@PathVariable("name", required =  false) name: String?): HttpEntity<PageResultEntity<FunctionLayerEntity>> {
        return ResponseEntity(functionService.getLayer(name ?: ""), HttpStatus.OK)
    }
    @PostMapping("/api/functions/create")
    suspend fun create(): HttpEntity<ResourceEntity> {
        val code = "exports.handler = async (event) => {    const response = {        statusCode: 200,        body: JSON.stringify('Hello from Lambda!'),    };    return response;};"
        val resource = functionService.addLambda("function3", code)
        return ResponseEntity(resource, HttpStatus.OK)
    }
}