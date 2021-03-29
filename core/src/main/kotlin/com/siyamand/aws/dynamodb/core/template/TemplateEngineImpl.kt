package com.siyamand.aws.dynamodb.core.template

import freemarker.template.Configuration
import freemarker.template.DefaultObjectWrapperBuilder
import freemarker.template.Template
import java.io.StringReader
import java.io.StringWriter
import java.io.Writer


class TemplateEngineImpl : TemplateEngine {
    override fun execute(template: String, context: Map<String, Any>): String {
        val cfg = Configuration(Configuration.VERSION_2_3_29)
        cfg.objectWrapper = DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_29).build()
        cfg.defaultEncoding = "UTF-8"
        val t = Template("templateName", StringReader(template), cfg)

        val out: Writer = StringWriter()
        t.process(context, out)

        return out.toString()
    }
}