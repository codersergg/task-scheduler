package com.codersergg.taskscheduler.configuration

import com.codersergg.taskscheduler.model.DeploymentUUID
import com.codersergg.taskscheduler.service.DeploymentUUIDService
import com.codersergg.taskscheduler.util.generateUUID7
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
import org.hibernate.Session
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Component
@Profile("dev", "prod")
class SchedulerCommandLineRunner(
    @PersistenceContext private val em: EntityManager,
    @Autowired private val deploymentUUIDService: DeploymentUUIDService
) : CommandLineRunner {

    val uuid: UUID = UUID7.uuid

    object UUID7 {
        val uuid: UUID = generateUUID7()
    }

    @Transactional
    override fun run(vararg args: String) {
        val deploymentUUID = uuid
        val session = em.unwrap(Session::class.java)
        session.persist(DeploymentUUID(deploymentUUID, Instant.now()))
        runBlocking {
            while (isActive) {
                deploymentUUIDService.updateByUUID()
                delay(5000)
            }
        }
    }
}