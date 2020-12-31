package com.siyamand.aws.dynamodb.core.entities.monitoring

enum class MetadataAttributes(val value: String) {
    SOURCE_TABLE_NAME("SOURCE_TABLE_NAME"),
    AGGREGATE_TABLE_NAME("AGGREGATE_TABLE_NAME"),
    MONITOR_TYPE("MONITOR_TYPE"),
    ATTRIBUTES("ATTRIBUTES"),
    STATUS("STATUS"),
    VERSION("VERSION"),
    MONITOR_TYPE_VERSION("MONITOR_TYPE_VERSION"),
    FUNCTION_NAME("FUNCTION_NAME"),
    ATTRIBUTE_NAME("ATTRIBUTE_NAME"),
    ATTRIBUTE_PATH("ATTRIBUTE_PATH"),
}