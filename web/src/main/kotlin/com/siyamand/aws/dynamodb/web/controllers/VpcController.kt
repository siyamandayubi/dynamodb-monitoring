package com.siyamand.aws.dynamodb.web.controllers

import com.siyamand.aws.dynamodb.core.sdk.network.EndpointEntity
import com.siyamand.aws.dynamodb.core.sdk.network.VpcService
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(maxAge = 3600)
@RestController
class VpcController(private val vpcService: VpcService) {
    @GetMapping("/api/vpc/endpoints")
    suspend fun getEndpoints(): HttpEntity<List<EndpointEntity>>{
        return ResponseEntity(vpcService.getEndpoints(), HttpStatus.OK)
    }
}