package com.siyamand.aws.dynamodb.infrastructure.repositories

import com.siyamand.aws.dynamodb.core.common.PageResultEntity
import com.siyamand.aws.dynamodb.core.sdk.s3.*
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilder
import com.siyamand.aws.dynamodb.infrastructure.mappers.S3Mapper
import kotlinx.coroutines.reactive.awaitFirst
import reactor.core.publisher.Mono
import software.amazon.awssdk.core.async.AsyncResponseTransformer
import software.amazon.awssdk.core.internal.async.ByteArrayAsyncRequestBody
import software.amazon.awssdk.core.internal.async.ByteArrayAsyncResponseTransformer
import software.amazon.awssdk.services.s3.model.*
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


class S3RepositoryImpl(private val clientBuilder: ClientBuilder) : S3Repository, AwsBaseRepositoryImpl() {
    override suspend fun addBucket(entity: CreateBucketRequestEntity): String {
        val client = getClient(clientBuilder::buildAsyncS3Client)
        val request = S3Mapper.convert(entity)
        val response = client.createBucket(request).thenApply { it.location() }
        return Mono.fromFuture(response).awaitFirst()
    }

    override suspend fun enableBucketVersioning(bucket: String): String {
        val client = getClient(clientBuilder::buildAsyncS3Client)
        val response = client.putBucketVersioning(PutBucketVersioningRequest
                .builder()
                .bucket(bucket)
                .versioningConfiguration(
                        VersioningConfiguration
                                .builder()
                                .status(BucketVersioningStatus.ENABLED).build())
                .build())
                .thenApply { "Success" }
        return Mono.fromFuture(response).awaitFirst()
    }

    override suspend fun getBuckets(): List<String> {
        val client = getClient(clientBuilder::buildAsyncS3Client)
        val response = client.listBuckets().thenApply { it.buckets().map { it.name() } }
        return Mono.fromFuture(response).awaitFirst()
    }


    override suspend fun getObject(bucket: String, prefix: String): S3ObjectEntityWithData {
        val client = getClient(clientBuilder::buildAsyncS3Client)
        val transformer = ByteArrayAsyncResponseTransformer<GetObjectResponse>()
        val response = client.getObject(
                GetObjectRequest.builder().bucket(bucket).key(prefix).build(), AsyncResponseTransformer.toBytes()
                )
                .thenApply {
                    val response = it.response()
                    S3ObjectEntityWithData(prefix,bucket,response.eTag()?:"", response.versionId(), it.asByteArray())
                }
        return Mono.fromFuture(response).awaitFirst()
    }

    override suspend fun getObjectVersions(bucket: String, prefix: String, marker: String): PageResultEntity<S3ObjectVersionEntity> {
        val client = getClient(clientBuilder::buildAsyncS3Client)
        val requestBuilder =
                ListObjectVersionsRequest
                        .builder()
                        .prefix(prefix)
                        .bucket(bucket)

        if (!marker.isNullOrEmpty()) {
            requestBuilder.keyMarker(marker)
        }

        val response = client.listObjectVersions(requestBuilder.build())
                .thenApply {
                    PageResultEntity<S3ObjectVersionEntity>(it.versions().map(S3Mapper::convert), it.nextKeyMarker()
                            ?: "")
                }
        return Mono.fromFuture(response).awaitFirst()
    }

    override suspend fun addObject(entity: CreateS3ObjectRequestEntity): S3ObjectEntity {
        val client = getClient(clientBuilder::buildAsyncS3Client)
        val tagging = entity.tags.map { "${it.key}=${encodeValue(it.value)}" }.joinToString("&")
        val requestBuilder = PutObjectRequest
                .builder()
                .bucket(entity.bucket)
                .key(entity.key)

        if (!tagging.isNullOrEmpty()) {
            requestBuilder.tagging(tagging)
        }

        val response = client.putObject(requestBuilder.build(), ByteArrayAsyncRequestBody(entity.data, entity.mimeType)).thenApply { S3ObjectEntity(entity.key, entity.bucket, it.eTag()) }

        return Mono.fromFuture(response).awaitFirst()
    }

    private fun encodeValue(value: String): String? {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString())
    }
}