package com.siyamand.aws.dynamodb.core.dynamodb

import java.time.Instant

class AttributeValueEntity {
    constructor() {
    }

    constructor(str: String) {
        stringValue = str
    }

    constructor(int: Int) {
        intValue = int
    }

    constructor(instant: Instant) {
        instantValue = instant
    }

    constructor(integers: Array<Int>) {
        intArrayValue = integers
    }

    constructor(strs: Array<String>) {
        stringArrayValue = strs
    }

    constructor(complexValue: Map<String, AttributeValueEntity>) {
        this.complexValue = complexValue
    }

    var stringValue: String? = null
        private set

    var boolValue: Boolean? = null
        private set

    var intValue: Int? = null
        private set

    var instantValue: Instant? = null
        private set

    var stringArrayValue: Array<String>? = null
        private set

    var intArrayValue: Array<Int>? = null
        private set

    var boolArrayValue: Array<Boolean>? = null
        private set

    var complexValue: Map<String, AttributeValueEntity>? = null
        private set

    val type: AttributeValueType
        get() {
            if (instantValue != null) {
                return AttributeValueType.DATE
            } else if (stringValue != null) {
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
            }

            return AttributeValueType.NULL
        }
}

