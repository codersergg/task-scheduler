package com.codersergg.taskscheduler.configuration

import com.codersergg.taskscheduler.model.Owner
import com.codersergg.taskscheduler.model.Task
import com.codersergg.taskscheduler.repository.OwnerRepository
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
        ownerRepository: OwnerRepository,
        taskRepository: TaskRepository
    ) = ApplicationRunner {
        val owner1 = ownerRepository.save(Owner("task name 1"))
        val owner2 = ownerRepository.save(Owner("task name 2"))
        val owner3 = ownerRepository.save(Owner("task name 3"))

        taskRepository.save(Task(owner1, Instant.now()))
        taskRepository.save(Task(owner1, Instant.now()))
        taskRepository.save(Task(owner1, Instant.now()))
        taskRepository.save(Task(owner2, Instant.now()))
        taskRepository.save(Task(owner3, Instant.now()))
    }
}