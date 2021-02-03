package com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring

enum class MonitorStatus(val value: Int) {
    PENDING(0),
    TABLE_CREATED(1),
    FUNCTION_CREATED(2),
    ACTIVE(3),
    SUSPEND(4)
}