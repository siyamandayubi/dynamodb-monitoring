package com.siyamand.aws.dynamodb.web.controllers

import com.siyamand.aws.dynamodb.core.sdk.s3.S3Service
import com.siyamand.aws.dynamodb.web.models.BucketListModel
import com.siyamand.aws.dynamodb.web.models.EnableBucketVersioningModel
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin(maxAge = 3600)
@RestController
class S3Controller(private val s3Service: S3Service) {

    @PostMapping("/api/s3/enableVersioning")
    suspend fun enableVersioning(@RequestBody model : EnableBucketVersioningModel): HttpEntity<String> {
        s3Service.enableBucketVersions(model.name)
        return ResponseEntity("Success", HttpStatus.OK)
    }

    @GetMapping("/api/s3/list")
    suspend fun getList(): HttpEntity<BucketListModel> {
        val buckets = s3Service.getBuckets()
        return ResponseEntity(BucketListModel(buckets), HttpStatus.OK)
    }
}