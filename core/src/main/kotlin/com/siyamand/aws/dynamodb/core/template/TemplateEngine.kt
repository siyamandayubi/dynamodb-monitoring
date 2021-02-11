package com.siyamand.aws.dynamodb.core.template

interface TemplateEngine {
    fun execute(template: String, context: Map<String, Any>): String
}