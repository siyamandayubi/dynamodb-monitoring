package com.siyamand.aws.dynamodb.core.entities

class PageResultEntity<T>(val items: List<T>, val nextPageToken: String?) {
}