package com.siyamand.aws.dynamodb.core.repositories

interface AWSCRUDRepository<T, out TOut> : AWSBaseRepository<TOut> {
    fun add(t: T)
    fun edit(t: T)
    fun delete(t: T)
    suspend fun getList(): List<T>
}