package com.siyamand.aws.dynamodb.core.repositories

import kotlinx.coroutines.flow.Flow
import com.siyamand.aws.dynamodb.core.entities.TableDetailEntity
import com.siyamand.aws.dynamodb.core.entities.TableEntity
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