package com.codersergg.taskscheduler.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
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

/*    @Bean(name = ["objMapper"])
    fun objMapper(): ObjectMapper = JsonMapper.builder()
        .addModule(ParameterNamesModule())
        .addModule(Jdk8Module())
        .addModule(JavaTimeModule())
        .build()*/
}