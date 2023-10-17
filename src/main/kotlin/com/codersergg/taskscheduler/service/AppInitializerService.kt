package com.codersergg.taskscheduler.service

import com.codersergg.taskscheduler.configuration.ApplicationProperties
import com.codersergg.taskscheduler.repository.AppInitializerRepository
import com.codersergg.taskscheduler.repository.TaskRepository
import com.codersergg.taskscheduler.scheduler.Scheduler
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service

@Service
class AppInitializerService(
    private val applicationProperties: ApplicationProperties,
    private val appInitializerRepository: AppInitializerRepository,
    private val taskRepository: TaskRepository,
    private val scheduler: Scheduler,
) {

    private val logger = KotlinLogging.logger {}
    suspend fun starting() {
        val isRegister = register()
        logger.info { "isRegister: $isRegister" }
        val allTasks = taskRepository.getAllTasksNotRun()
        withContext(Dispatchers.IO) {
            allTasks.map {
                scheduler.run({ println("Task saved Run") }, it.toAbstractTask())
            }
        }

        withContext(Dispatchers.IO) {
            while (isActive) {
                updateLastActivity()
                delay(applicationProperties.updateTime.toLong())
            }
        }
        withContext(Dispatchers.IO) {
            delay(applicationProperties.updateTime.toLong() * 2)
            deleteUnused()
        }
    }

    private fun register(): Boolean {
        val isRegister = appInitializerRepository.register()
        if (!isRegister) {
            throw ExceptionInInitializerError("DeploymentUUID is not registered")
        } else {
            return isRegister
        }

    }

    private fun updateLastActivity() {
        appInitializerRepository.updateLastActivity()

    }

    private fun deleteUnused() {
        appInitializerRepository.delete()
    }
}