package com.codersergg.taskscheduler.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "task-scheduler.deployment-uuid")
data class ApplicationProperties(val updateTime: String = "")