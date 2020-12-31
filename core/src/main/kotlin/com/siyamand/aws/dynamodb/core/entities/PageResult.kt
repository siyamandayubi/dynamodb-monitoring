package com.siyamand.aws.dynamodb.core.entities

class PageResult<T>(val items: List<T>, val nextPageToken: String) {
}