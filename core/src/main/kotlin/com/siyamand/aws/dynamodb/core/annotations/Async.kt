package com.siyamand.aws.dynamodb.core.annotations

import java.lang.annotation.*
import java.lang.annotation.Retention

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(RetentionPolicy.RUNTIME)
annotation class Async {
}