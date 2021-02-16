package com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring

enum class MonitorStatus(val value: Int) {
    INITIAL(0),
    PENDING(1),
    SUSPEND(2),
    ACTIVE(3),
    DISABLED(4),
    ERROR(5)
}