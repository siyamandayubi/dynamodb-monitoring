package com.siyamand.aws.dynamodb.core.dynamodb

import com.siyamand.aws.dynamodb.core.common.AWSBaseRepository
import org.springframework.stereotype.Component

@Component
interface TableRepository : AWSBaseRepository {
    // Read this one
    // https://www.baeldung.com/spring-boot-kotlin-coroutines
    suspend fun getDetail(tableName: String): TableDetailEntity?
    suspend fun getList(): List<TableEntity>
    suspend fun add(t: TableDetailEntity)
    suspend fun enableStream(tableName: String): TableDetailEntity?
}