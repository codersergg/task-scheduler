package com.codersergg.taskscheduler.configuration

import com.codersergg.taskscheduler.model.Task
import com.codersergg.taskscheduler.repository.TaskRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.sql.Timestamp
import java.time.Instant

@Configuration
class ConfigurationTest {
    @Bean
    fun databaseInitializerTest(
        taskRepository: TaskRepository
    ) = ApplicationRunner {
        taskRepository.save(Task("task name 1", Timestamp.from(Instant.now())))
        taskRepository.save(Task("task name 2", Timestamp.from(Instant.now())))
        taskRepository.save(Task("task name 3", Timestamp.from(Instant.now())))
    }
}