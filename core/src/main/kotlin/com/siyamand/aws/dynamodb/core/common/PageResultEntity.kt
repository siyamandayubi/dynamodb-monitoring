package com.siyamand.aws.dynamodb.core.common

open class PageResultEntityBase<T, KEY>(val items: List<T>, val nextPageToken: KEY?) {
}

class PageResultEntity<T>(items: List<T>, nextPageToken: String) : PageResultEntityBase<T, String>(items, nextPageToken)