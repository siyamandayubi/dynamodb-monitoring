package com.siyamand.aws.dynamodb.core.common

class PageResultEntity<T>(val items: List<T>, val nextPageToken: String?) {
}