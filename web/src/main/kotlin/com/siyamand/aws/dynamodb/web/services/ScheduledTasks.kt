package com.siyamand.aws.dynamodb.web.services

import com.siyamand.aws.dynamodb.core.schedule.WorkflowJobHandler
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component


@Component
class ScheduledTasks(private val workflowJobHandler: WorkflowJobHandler) {

    var logger: Logger = LoggerFactory.getLogger(javaClass)

    //@Scheduled(fixedRate = 5000)
    fun executeWorkflow() {
        runBlocking {
            logger.info("running task")
        }
    }
}