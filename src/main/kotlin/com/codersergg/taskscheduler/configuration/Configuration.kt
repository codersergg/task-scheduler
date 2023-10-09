package com.codersergg.taskscheduler.configuration

import com.codersergg.taskscheduler.repository.OwnerRepository
import com.codersergg.taskscheduler.repository.TaskRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaRepositories


@org.springframework.context.annotation.Configuration
@EnableJpaRepositories("com.codersergg.taskscheduler.repository")
@EntityScan("com.codersergg.taskscheduler.model")
class Configuration {
    @Bean
    fun databaseInitializer(
        ownerRepository: OwnerRepository,
        taskRepository: TaskRepository
    ) = ApplicationRunner {
        /*val owner1 = ownerRepository.save(Owner("task name 1"))
        val owner2 = ownerRepository.save(Owner("task name 2"))
        val owner3 = ownerRepository.save(Owner("task name 3"))

        taskRepository.save(Task(owner1, Instant.now()))
        taskRepository.save(Task(owner1, Instant.now()))
        taskRepository.save(Task(owner1, Instant.now()))
        taskRepository.save(Task(owner2, Instant.now()))
        taskRepository.save(Task(owner3, Instant.now()))*/
    }
}