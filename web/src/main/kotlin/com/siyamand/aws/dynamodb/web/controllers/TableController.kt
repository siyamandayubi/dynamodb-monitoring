package com.siyamand.aws.dynamodb.web.controllers

import ch.qos.logback.classic.db.names.TableName
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.reactor.mono
import com.siyamand.aws.dynamodb.core.entities.TableDetailEntity
import com.siyamand.aws.dynamodb.core.entities.TableEntity
import com.siyamand.aws.dynamodb.core.services.TableService
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import kotlinx.coroutines.launch
import java.net.http.HttpResponse

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