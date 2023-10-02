package com.codersergg.taskscheduler

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TaskSchedulerApplication

fun main(args: Array<String>) {
	runApplication<TaskSchedulerApplication>(*args)
}
