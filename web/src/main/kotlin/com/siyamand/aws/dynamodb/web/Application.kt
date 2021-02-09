package com.siyamand.aws.dynamodb.web

import com.siyamand.aws.dynamodb.core.config.CoreConfiguration
import com.siyamand.aws.dynamodb.infrastructure.config.InfrastructureConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication()
@EnableScheduling
@Import(InfrastructureConfiguration::class, CoreConfiguration::class)
open class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
