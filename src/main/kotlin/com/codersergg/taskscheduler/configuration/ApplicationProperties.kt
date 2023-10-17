package com.codersergg.taskscheduler.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "task-scheduler.app-initializer")
data class ApplicationProperties(val updateTime: String = "")