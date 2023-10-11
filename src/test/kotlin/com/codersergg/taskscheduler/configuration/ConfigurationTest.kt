package com.codersergg.taskscheduler.configuration

import com.codersergg.taskscheduler.model.Provider
import com.codersergg.taskscheduler.model.Task
import com.codersergg.taskscheduler.repository.ProviderRepository
import com.codersergg.taskscheduler.repository.TaskRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Instant

@Configuration
class ConfigurationTest {
    @Bean
    fun databaseInitializerTest(
        providerRepository: ProviderRepository,
        taskRepository: TaskRepository
    ) = ApplicationRunner {
        val owner1 = providerRepository.save(Provider("task name 1"))
        val owner2 = providerRepository.save(Provider("task name 2"))
        val owner3 = providerRepository.save(Provider("task name 3"))

        taskRepository.save(Task(owner1, Instant.now(), Instant.EPOCH, 5.toString()))
        taskRepository.save(Task(owner1, Instant.now(), Instant.EPOCH, 5.toString()))
        taskRepository.save(Task(owner1, Instant.now(), Instant.EPOCH, 5.toString()))
        taskRepository.save(Task(owner2, Instant.now(), Instant.EPOCH, 5.toString()))
        taskRepository.save(Task(owner3, Instant.now(), Instant.EPOCH, 5.toString()))
    }
}