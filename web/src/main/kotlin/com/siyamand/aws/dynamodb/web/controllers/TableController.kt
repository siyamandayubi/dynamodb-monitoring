package com.siyamand.aws.dynamodb.web.controllers

import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.reactor.mono
import com.siyamand.aws.dynamodb.core.dynamodb.TableDetailEntity
import com.siyamand.aws.dynamodb.core.dynamodb.TableEntity
import com.siyamand.aws.dynamodb.core.dynamodb.TableService
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class TableController(private val tableService: TableService) {
    @GetMapping("/api/tables")
    suspend fun getTables(): HttpEntity<Mono<List<TableEntity>>> {
        return ResponseEntity(Mono.just(tableService.getTables()), HttpStatus.OK)
    }

    @GetMapping("/api/tables/{name}")
    fun getTableDetail(@PathVariable name: String) = mono(Unconfined) {
        //return ResponseEntity(tableService.getTableDetail(tableName), HttpStatus.OK)
        tableService.getTableDetail(name)
    }

    @GetMapping("/api/tables1/{name}")
    suspend fun getTableDetail1(@PathVariable("name") name: String): HttpEntity<TableDetailEntity> {
        //return ResponseEntity(tableService.getTableDetail(tableName), HttpStatus.OK)
        val entity = tableService.getTableDetail(name)
        return ResponseEntity(entity, HttpStatus.OK)
    }
}