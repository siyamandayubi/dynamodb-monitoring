package com.siyamand.aws.dynamodb.core.monitoring

import com.siyamand.aws.dynamodb.core.sdk.dynamodb.TableAttribute
import com.siyamand.aws.dynamodb.core.sdk.dynamodb.TableDetailEntity
import com.siyamand.aws.dynamodb.core.sdk.dynamodb.TableKeyScheme

class MonitoringTableBuilderImpl : MonitoringTableBuilder {
    override fun build(tableName: String): TableDetailEntity {

        return TableDetailEntity(
                "",
                tableName,
                mutableListOf(
                        TableAttribute("id", "S")
                        //TableAttribute("sourceTable", "S"),
                        //TableAttribute("type", "S"),
                        //TableAttribute("status", "S"),
                        //TableAttribute("version", "N"),
                        //TableAttribute("workflow", "S"),
                ),
                mutableListOf(TableKeyScheme("id", "HASH")),
                "",
        false,
        null)
    }
}