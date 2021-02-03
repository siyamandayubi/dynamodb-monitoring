package com.siyamand.aws.dynamodb.core.monitoring.entities.item

class AttributeValueEntity {
    constructor() {
    }
    constructor(str: String) {
        stringValue = str
    }

    constructor(int: Int) {
        intValue = int
    }
    constructor(integers: Array<Int>) {
        intArrayValue = integers
    }
    constructor(strs: Array<String>) {
        stringArrayValue = strs
    }
    constructor(complexValues: Array<Map<String,AttributeValueEntity>>){
        this.complexArrayValue = complexValues
    }
    constructor(complexValue: Map<String,AttributeValueEntity>){
        this.complexValue = complexValue
    }
    var stringValue: String? = null
    var boolValue: Boolean? = null
    var intValue: Int? = null
    var stringArrayValue: Array<String>? = null
    var intArrayValue: Array<Int>? = null
    var boolArrayValue: Array<Boolean>? = null
    var complexValue: Map<String, AttributeValueEntity>? = null
    var complexArrayValue: Array<Map<String, AttributeValueEntity>>? = null

    val type: AttributeValueType
        get() {
            if (stringValue != null) {
                return AttributeValueType.STRING
            } else if (intValue != null) {
                return AttributeValueType.INT
            } else if (boolValue != null) {
                return AttributeValueType.BOOL
            } else if (stringArrayValue != null) {
                return AttributeValueType.STRING_ARRAY
            } else if (boolArrayValue != null) {
                return AttributeValueType.BOOL_ARRAY
            } else if (intArrayValue != null) {
                return AttributeValueType.INT_ARRAY
            } else if (complexValue != null) {
                return AttributeValueType.COMPLEX
            } else if (complexArrayValue != null) {
                return AttributeValueType.COMPLEX_ARRAY
            }

            return AttributeValueType.NULL
        }
}

