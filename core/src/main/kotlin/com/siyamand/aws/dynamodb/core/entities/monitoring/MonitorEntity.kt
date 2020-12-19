package com.siyamand.aws.dynamodb.core.entities.monitoring

class MonitorEntity(var sourceTableName: String) {
    private val aggregateFields: MutableList<AggregateFieldEntity> = mutableListOf()

    fun addField(aggregateFieldEntity: AggregateFieldEntity) {
        aggregateFields.add(aggregateFieldEntity)
    }

    fun removeField(aggregateFieldEntity: AggregateFieldEntity) {
        aggregateFields.remove(aggregateFieldEntity)
    }

    fun getAggregateFields(): List<AggregateFieldEntity> {
        return aggregateFields.toList()
    }
}