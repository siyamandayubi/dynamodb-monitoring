package com.siyamand.aws.dynamodb.core.sdk.rds

import com.siyamand.aws.dynamodb.core.sdk.rds.entities.RdsAppConfigItemEntitySerializer
import com.siyamand.aws.dynamodb.core.sdk.rds.entities.RdsProxyEntity
import com.siyamand.aws.dynamodb.core.sdk.resource.ResourceEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
internal class RdsConfigBuilderImplTest{
    @Test
    fun test_create_with_two_instance(){
        val classUnderTest = RdsConfigBuilderImpl()
        val input = listOf(
                RdsProxyEntity("p1", ResourceEntity("","","","",""),"S","S","R", "E1"),
                RdsProxyEntity("p1", ResourceEntity("","","","",""),"S","S", "R", "E2"))
        val result = classUnderTest.create(input, "Dbname")
        assertEquals(result.endpoints[0].start , "000")
        assertEquals(result.endpoints[1].start , "800")
        assertEquals(result.endpoints[0].end , "7ff")
        assertEquals(result.endpoints[1].end , "fff")
    }

    @Test
    fun test_create_with_two_instance_encoding(){
        val classUnderTest = RdsConfigBuilderImpl()
        val input = listOf(
                RdsProxyEntity("p1", ResourceEntity("","","","",""),"S","S","R", "E1"),
                RdsProxyEntity("p1", ResourceEntity("","","","",""),"S","S", "R", "E2"))
        val result = classUnderTest.create(input, "Dbname")
        val json = Json.encodeToString(result)
        assertNotNull(json)
    }
}