package com.codersergg.taskscheduler.service

import com.codersergg.taskscheduler.configuration.SchedulerCommandLineRunner
import com.codersergg.taskscheduler.model.DeploymentUUID
import com.codersergg.taskscheduler.model.DeploymentUUID_
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.persistence.criteria.CriteriaUpdate
import org.hibernate.Session
import org.hibernate.query.criteria.HibernateCriteriaBuilder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Component
@Transactional
class DeploymentUUIDService(@PersistenceContext private val em: EntityManager) {

    val uuid: UUID = SchedulerCommandLineRunner.UUID7.uuid

    fun updateByUUID(): Int {
        val session = em.unwrap(Session::class.java)
        val builder: HibernateCriteriaBuilder = session.criteriaBuilder
        val update: CriteriaUpdate<DeploymentUUID> = builder.createCriteriaUpdate(DeploymentUUID::class.java)
        val deploymentUUIDRoot = update.from(DeploymentUUID::class.java)
        update.where(builder.equal(deploymentUUIDRoot[DeploymentUUID_.uuid], uuid))
        update.set(DeploymentUUID_.lastActivity, Instant.now())
        val executeUpdate = session.createMutationQuery(update).executeUpdate()
        session.clear()
        return executeUpdate
    }

    fun getByUUID(): DeploymentUUID {
        val session = em.unwrap(Session::class.java)
        val criteriaBuilder = session.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(DeploymentUUID::class.java)
        val root = criteriaQuery.from(DeploymentUUID::class.java)
        val uuidPath = root.get<UUID>(DeploymentUUID_.UUID)
        criteriaQuery.where(criteriaBuilder.equal(uuidPath, uuid))
        val query = session.createQuery(criteriaQuery)
        val result = query.singleResult
        return result as DeploymentUUID
    }

}
