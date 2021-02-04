package com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring

enum class MonitorStatus(val value: Int) {
    PENDING(0),
    SUSPEND(1),
    ACTIVE(2),
    DISABLED(3)
}