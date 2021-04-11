package com.siyamand.aws.dynamodb.web.controllers

import com.siyamand.aws.dynamodb.core.sdk.network.RegionEntity
import com.siyamand.aws.dynamodb.core.sdk.network.VpcService
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.net.http.HttpResponse

@CrossOrigin(maxAge = 3600)
@RestController
class BasicDataController(private val vpcService: VpcService) {
    @GetMapping("/api/basic-data/regions")
    suspend fun getRegions(): HttpEntity<List<RegionEntity>> {
        return ResponseEntity(vpcService.getRegions(), HttpStatus.OK)
    }
}