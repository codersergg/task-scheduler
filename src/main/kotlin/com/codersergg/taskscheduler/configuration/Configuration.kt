package com.codersergg.taskscheduler.configuration

import com.codersergg.taskscheduler.repository.ProviderRepository
import com.codersergg.taskscheduler.repository.TaskRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@org.springframework.context.annotation.Configuration
@EnableJpaRepositories("com.codersergg.taskscheduler.repository")
@EntityScan("com.codersergg.taskscheduler.model")
@Profile("!test")
class Configuration {
    @Bean
    fun databaseInitializer(
        providerRepository: ProviderRepository,
        taskRepository: TaskRepository
    ) = ApplicationRunner {
        /*val provider1 = providerRepository.save(Provider("provider name 1"))
        val provider2 = providerRepository.save(Provider("provider name 2"))
        val provider3 = providerRepository.save(Provider("provider name 3"))

        val createdAt = Instant.now()
        val save = taskRepository.saveAndFlush(Task(provider1, createdAt, Instant.EPOCH, Duration(5)))
        //save.delay = Timer(1, 2, 11, 30)
        //taskRepository.saveAndFlush(save)
        val taskToSave = Task(provider1, Instant.now(), Instant.EPOCH, Duration(5))
        taskRepository.saveAndFlush(taskToSave).toTaskResponseWithDelay()
        taskRepository.saveAndFlush(Task(provider1, Instant.now(), Instant.EPOCH, Duration(5)))
        taskRepository.saveAndFlush(Task(provider2, Instant.now(), Instant.EPOCH, Duration(5)))
        taskRepository.saveAndFlush(Task(provider3, Instant.now(), Instant.EPOCH, Timer(1, 2, 11, 30)))
*/


        val tasks = taskRepository.findAllByOrderById()
        tasks.forEach { println(it.toTaskResponseWithDelay()) }
    }
}