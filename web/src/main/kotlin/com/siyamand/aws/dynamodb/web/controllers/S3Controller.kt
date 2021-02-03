package com.siyamand.aws.dynamodb.web.controllers

import com.siyamand.aws.dynamodb.core.entities.s3.S3ObjectEntity
import com.siyamand.aws.dynamodb.core.services.S3Service
import com.siyamand.aws.dynamodb.web.models.BucketListModel
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class S3Controller(private val s3Service: S3Service) {
    @PostMapping("/api/s3/create-bucket")
    suspend fun createBucket(): HttpEntity<String> {
        val location = s3Service.createBucket()
        return ResponseEntity(location, HttpStatus.OK)
    }

    @PostMapping("/api/s3/create-object")
    suspend fun createObject(): HttpEntity<S3ObjectEntity> {
        val code = "exports.handler = async (event) => {    const response = {        statusCode: 200,        body: JSON.stringify('Hello from Lambda!'),    };    return response;};"
       val obj =  s3Service.addObject("function3.zip",code)
        return ResponseEntity(obj, HttpStatus.OK)
    }
    @GetMapping("/api/s3/list")
    suspend fun getList(): HttpEntity<BucketListModel> {
        val buckets = s3Service.getBuckets()
        return ResponseEntity(BucketListModel(buckets), HttpStatus.OK)
    }
}