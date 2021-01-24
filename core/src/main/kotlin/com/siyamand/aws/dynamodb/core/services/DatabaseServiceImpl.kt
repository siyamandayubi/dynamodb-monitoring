package com.siyamand.aws.dynamodb.core.services

import com.siyamand.aws.dynamodb.core.repositories.DatabaseRepository

class DatabaseServiceImpl(private val monitorConfigProvider: MonitorConfigProvider,private val databaseRepository: DatabaseRepository) : DatabaseService {

    fun createDatabase(){

    }
}