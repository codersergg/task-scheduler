package com.codersergg.taskscheduler

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class TaskSchedulerApplication

fun main(args: Array<String>) {
    runApplication<TaskSchedulerApplication>(*args)
}
