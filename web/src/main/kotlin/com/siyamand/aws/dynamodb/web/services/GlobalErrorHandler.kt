package com.siyamand.aws.dynamodb.web.services

import com.siyamand.aws.dynamodb.core.workflow.WorkflowManagerImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Configuration
@Order(-2)
open class GlobalErrorHandler : ErrorWebExceptionHandler {
    var logger: Logger = LoggerFactory.getLogger(WorkflowManagerImpl::class.java)

    override fun handle(serverWebExchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        val response = serverWebExchange.response
        val bufferFactory: DataBufferFactory = response.bufferFactory()

        logger.error("message=${ex.message}. stack=${ex.stackTrace}")
        response.statusCode = HttpStatus.INTERNAL_SERVER_ERROR
        response.headers.contentType = MediaType.TEXT_PLAIN
        val dataBuffer: DataBuffer = bufferFactory.wrap("Unknown error".toByteArray())
        return response.writeWith(Mono.just(dataBuffer))
    }
}