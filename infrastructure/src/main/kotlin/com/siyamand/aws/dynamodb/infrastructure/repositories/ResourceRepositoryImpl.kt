package com.siyamand.aws.dynamodb.infrastructure.repositories

import com.siyamand.aws.dynamodb.core.entities.PageResult
import com.siyamand.aws.dynamodb.core.entities.ResourceEntity
import com.siyamand.aws.dynamodb.core.repositories.ResourceRepository
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilder
import com.siyamand.aws.dynamodb.infrastructure.mappers.CredentialMapper
import com.siyamand.aws.dynamodb.infrastructure.mappers.ResourceMapper
import software.amazon.awssdk.services.resourcegroupstaggingapi.ResourceGroupsTaggingApiClient
import software.amazon.awssdk.services.resourcegroupstaggingapi.model.GetResourcesRequest
import software.amazon.awssdk.services.resourcegroupstaggingapi.model.TagFilter

class ResourceRepositoryImpl(private val clientBuilder: ClientBuilder) : ResourceRepository, AwsBaseRepositoryImpl() {
    override fun getResources(tagName: String, tagValue: String, nextPageToken: String): PageResult<ResourceEntity> {
        val client = asyncResourceClient()

        // request builder
        val requestBuilder = GetResourcesRequest
                .builder()
                .tagFilters(TagFilter.builder().key(tagName).values(tagValue).build())

        // pagination token
        if (!nextPageToken?.isNullOrEmpty()){
            requestBuilder.paginationToken(nextPageToken)
        }

        val response = client.getResources(requestBuilder.build())

        var items = response.resourceTagMappingList().map { ResourceMapper.convert(it) }

        return PageResult(items, response.paginationToken())
    }

    private fun asyncResourceClient(): ResourceGroupsTaggingApiClient {
        if (this.token == null) {
            throw IllegalArgumentException("token is not provider")
        }

        if (this.region.isNullOrEmpty()) {
            throw java.lang.IllegalArgumentException("region is not provider")
        }

        val credential = CredentialMapper.convert(this.token!!)
        return clientBuilder.builcResourceGroupsTaggingApiClient(region, credential)
    }
}