package com.codersergg.taskscheduler.configuration

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories


@org.springframework.context.annotation.Configuration
@EnableJpaRepositories("com.codersergg.taskscheduler.repository")
@EntityScan("com.codersergg.taskscheduler.model")
class Configuration {
    /*@Bean
    fun databaseInitializer(
        providerRepository: ProviderRepository,
        taskRepository: TaskRepository
    ) = ApplicationRunner {
        val owner1 = providerRepository.save(Provider("task name 1"))
        val owner2 = providerRepository.save(Provider("task name 2"))
        val owner3 = providerRepository.save(Provider("task name 3"))

        taskRepository.save(Task(owner1, Instant.now(), Instant.EPOCH))
        taskRepository.save(Task(owner1, Instant.now(), Instant.EPOCH))
        taskRepository.save(Task(owner1, Instant.now(), Instant.EPOCH))
        taskRepository.save(Task(owner2, Instant.now(), Instant.EPOCH))
        taskRepository.save(Task(owner3, Instant.now(), Instant.EPOCH))
    }*/
}