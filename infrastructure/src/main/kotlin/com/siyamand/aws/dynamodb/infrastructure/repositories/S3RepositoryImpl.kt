package com.siyamand.aws.dynamodb.infrastructure.repositories

import com.siyamand.aws.dynamodb.core.s3.CreateBucketRequestEntity
import com.siyamand.aws.dynamodb.core.s3.CreateS3ObjectRequestEntity
import com.siyamand.aws.dynamodb.core.s3.S3ObjectEntity
import com.siyamand.aws.dynamodb.core.s3.S3Repository
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilder
import com.siyamand.aws.dynamodb.infrastructure.mappers.S3Mapper
import kotlinx.coroutines.reactive.awaitFirst
import reactor.core.publisher.Mono
import software.amazon.awssdk.core.internal.async.ByteArrayAsyncRequestBody
import software.amazon.awssdk.services.s3.model.PutObjectRequest


class S3RepositoryImpl(private val clientBuilder: ClientBuilder) : S3Repository, AwsBaseRepositoryImpl() {
    override suspend fun addBucket(entity: CreateBucketRequestEntity): String {
        val client = getClient(clientBuilder::buildAsyncS3Client)
        val request = S3Mapper.convert(entity)
        val response = client.createBucket(request).thenApply { it.location() }
        return Mono.fromFuture(response).awaitFirst()
    }

    override suspend fun getBuckets(): List<String> {
        val client = getClient(clientBuilder::buildAsyncS3Client)
        val response = client.listBuckets().thenApply { it.buckets().map { it.name() } }
        return Mono.fromFuture(response).awaitFirst()
    }

    override suspend fun addObject(entity: CreateS3ObjectRequestEntity): S3ObjectEntity {
        val client = getClient(clientBuilder::buildAsyncS3Client)
        val response = client.putObject(
                PutObjectRequest
                        .builder()
                        .bucket(entity.bucket)
                        .key(entity.key)
                        .build(),
                ByteArrayAsyncRequestBody(entity.data))
                .thenApply { S3ObjectEntity(it.eTag()) }

        return Mono.fromFuture(response).awaitFirst()
    }
}