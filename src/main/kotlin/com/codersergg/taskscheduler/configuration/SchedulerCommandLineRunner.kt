package com.codersergg.taskscheduler.configuration

import com.codersergg.taskscheduler.service.AppInitializerService
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.runBlocking
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("dev", "prod")
class SchedulerCommandLineRunner(private val appInitializerService: AppInitializerService) : CommandLineRunner {
    private val logger = KotlinLogging.logger {}
    override fun run(vararg args: String) {
        runBlocking {
            logger.info { "appInitializerService.starting()" }
            appInitializerService.starting()
        }
    }
}