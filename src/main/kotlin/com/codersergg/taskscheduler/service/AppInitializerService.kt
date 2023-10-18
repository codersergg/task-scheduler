package com.codersergg.taskscheduler.service

import com.codersergg.taskscheduler.configuration.ApplicationProperties
import com.codersergg.taskscheduler.repository.AppInitializerRepository
import com.codersergg.taskscheduler.repository.TaskRepository
import com.codersergg.taskscheduler.scheduler.Scheduler
import com.codersergg.taskscheduler.util.SchedulerHttpClient
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import java.net.URI

@Service
class AppInitializerService(
    private val applicationProperties: ApplicationProperties,
    private val appInitializerRepository: AppInitializerRepository,
    private val taskRepository: TaskRepository,
    private val scheduler: Scheduler,
) {

    private val logger = KotlinLogging.logger {}

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun starting() {
        val isRegister = register()
        logger.info { "isRegister: $isRegister" }
        withContext(Dispatchers.IO) {
            val allTasks = taskRepository.getAllTasksNotRun()
            allTasks.forEach { task ->
                scheduler.run({
                    SchedulerHttpClient.sendRequest(
                        task.pathResponse.path as URI,
                        HttpMethod.POST,
                        body = task.toAbstractTask()
                    )
                }, task.toAbstractTask())
            }
        }

        GlobalScope.launch {
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