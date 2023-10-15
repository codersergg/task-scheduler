package com.codersergg.taskscheduler.configuration

import com.codersergg.taskscheduler.service.DeploymentUUIDService
import com.codersergg.taskscheduler.util.generateUUID7
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.util.*

@Component
@Profile("dev", "prod")
class SchedulerCommandLineRunner(
    private val applicationProperties: ApplicationProperties,
    @Autowired private val deploymentUUIDService: DeploymentUUIDService
) : CommandLineRunner {

    object UUID7 {
        val uuid: UUID = generateUUID7()
    }

    override fun run(vararg args: String) {
        if (!deploymentUUIDService.register()) {
            throw ExceptionInInitializerError("DeploymentUUID is not registered")
        }
        runBlocking {
            while (isActive) {
                println("count before: ${deploymentUUIDService.count()}")
                deploymentUUIDService.updateByUUID()
                delay(applicationProperties.updateTime.toLong())
                deploymentUUIDService.delete()
                println("count after: ${deploymentUUIDService.count()}")
            }
        }
    }
}