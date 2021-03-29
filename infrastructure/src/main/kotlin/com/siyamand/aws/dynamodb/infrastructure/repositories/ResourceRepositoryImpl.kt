package com.siyamand.aws.dynamodb.infrastructure.repositories

import com.siyamand.aws.dynamodb.core.common.PageResultEntity
import com.siyamand.aws.dynamodb.core.sdk.resource.ResourceEntity
import com.siyamand.aws.dynamodb.core.sdk.resource.ResourceRepository
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilder
import com.siyamand.aws.dynamodb.infrastructure.mappers.CredentialMapper
import com.siyamand.aws.dynamodb.infrastructure.mappers.ResourceMapper
import software.amazon.awssdk.services.resourcegroupstaggingapi.ResourceGroupsTaggingApiClient
import software.amazon.awssdk.services.resourcegroupstaggingapi.model.GetResourcesRequest
import software.amazon.awssdk.services.resourcegroupstaggingapi.model.TagFilter

class ResourceRepositoryImpl(private val clientBuilder: ClientBuilder) : ResourceRepository, AwsBaseRepositoryImpl() {
    override fun getResources(tagName: String, tagValue: String, types: Array<String>?, nextPageToken: String?): PageResultEntity<ResourceEntity> {
        val client = asyncResourceClient()

        val requestBuilder = GetResourcesRequest.builder()

        // Tag filter
        requestBuilder.tagFilters(TagFilter.builder().key(tagName).values(tagValue).build())

        // Types
        if (types != null && types.isNotEmpty()) {
            requestBuilder.resourceTypeFilters(*types)
        }

        // pagination token
        if (!nextPageToken.isNullOrEmpty()) {
            requestBuilder.paginationToken(nextPageToken)
        }

        val response = client.getResources(requestBuilder.build())

        val items = response.resourceTagMappingList().map { ResourceMapper.convert(it) }

        return PageResultEntity(items, response.paginationToken())
    }

    override fun convert(arn: String): ResourceEntity {
        return ResourceMapper.convert(arn)
    }

    private fun asyncResourceClient(): ResourceGroupsTaggingApiClient {
        if (this.token == null) {
            throw IllegalArgumentException("token is not provider")
        }

        if (this.region.isEmpty()) {
            throw java.lang.IllegalArgumentException("region is not provider")
        }

        val credential = CredentialMapper.convert(this.token!!)
        return clientBuilder.builcResourceGroupsTaggingApiClient(region, credential)
    }
}