package com.siyamand.aws.dynamodb.web.controllers

import com.siyamand.aws.dynamodb.core.entities.network.EndpointEntity
import com.siyamand.aws.dynamodb.core.services.VpcService
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.net.http.HttpResponse

@RestController
class VpcController(private val vpcService: VpcService) {
    @GetMapping("/api/vpc/endpoints")
    suspend fun getEndpoints(): HttpEntity<List<EndpointEntity>>{
        return ResponseEntity(vpcService.getEndpoints(), HttpStatus.OK)
    }
}