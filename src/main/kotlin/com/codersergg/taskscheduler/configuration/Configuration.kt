package com.codersergg.taskscheduler.configuration

import com.codersergg.taskscheduler.repository.TaskRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Configuration {
    @Bean
    fun databaseInitializer(
        taskRepository: TaskRepository
    ) = ApplicationRunner {
        /*taskRepository.save(Task("task name 1", 1))
        taskRepository.save(Task("task name 2", 2))*/
    }
}