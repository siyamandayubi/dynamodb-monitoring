package com.siyamand.aws.dynamodb.core.common

import java.lang.Exception

class NotExistException : Exception {
    constructor() : super()
    constructor(exception: Throwable) : super(exception)
}