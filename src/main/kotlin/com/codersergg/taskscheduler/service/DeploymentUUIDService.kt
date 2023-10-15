package com.codersergg.taskscheduler.service

import com.codersergg.taskscheduler.configuration.ApplicationProperties
import com.codersergg.taskscheduler.configuration.SchedulerCommandLineRunner
import com.codersergg.taskscheduler.model.DeploymentUUID
import com.codersergg.taskscheduler.model.DeploymentUUID_
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.persistence.criteria.CriteriaDelete
import jakarta.persistence.criteria.CriteriaUpdate
import org.hibernate.Session
import org.hibernate.query.criteria.HibernateCriteriaBuilder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*


@Component
class DeploymentUUIDService(
    @PersistenceContext private val em: EntityManager,
    private val applicationProperties: ApplicationProperties,
) {
    private val session: Session = em.unwrap(Session::class.java)
    private val uuid: UUID = SchedulerCommandLineRunner.UUID7.uuid

    @Transactional
    fun register(): Boolean {
        val deploymentUUID = DeploymentUUID(uuid, Instant.now())
        session.persist(deploymentUUID)
        println("register(): ${deploymentUUID.id}")
        println("register(): ${deploymentUUID.uuid}")
        session.flush()
        session.clear()
        return deploymentUUID.id != null
    }

    @Transactional
    fun updateByUUID(): Int {
        val builder: HibernateCriteriaBuilder = session.criteriaBuilder
        val update: CriteriaUpdate<DeploymentUUID> = builder.createCriteriaUpdate(DeploymentUUID::class.java)
        val deploymentUUIDRoot = update.from(DeploymentUUID::class.java)
        update.where(builder.equal(deploymentUUIDRoot[DeploymentUUID_.uuid], uuid))
        update.set(DeploymentUUID_.lastActivity, Instant.now())
        val executeUpdate = session.createMutationQuery(update).executeUpdate()
        session.flush()
        session.clear()
        return executeUpdate
    }

    fun getCurrent(): DeploymentUUID {
        return deploymentUUID(uuid)
    }

    fun getByUUID(uuid: UUID): DeploymentUUID {
        return deploymentUUID(uuid)
    }

    private fun deploymentUUID(uuid: UUID): DeploymentUUID {
        val criteriaBuilder = session.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(DeploymentUUID::class.java)
        val root = criteriaQuery.from(DeploymentUUID::class.java)
        val uuidPath = root.get<UUID>(DeploymentUUID_.UUID)
        criteriaQuery.where(criteriaBuilder.equal(uuidPath, uuid))
        val query = session.createQuery(criteriaQuery)
        val result = query.singleResult
        return result as DeploymentUUID
    }

    @Transactional
    fun delete() {
        val session = em.unwrap(Session::class.java)
        val builder: HibernateCriteriaBuilder = session.criteriaBuilder
        val delete: CriteriaDelete<DeploymentUUID> = builder.createCriteriaDelete(DeploymentUUID::class.java)
        val deploymentUUIDRoot = delete.from(DeploymentUUID::class.java)
        delete.where(
            builder.lessThan(
                deploymentUUIDRoot[DeploymentUUID_.lastActivity],
                Instant.now().minusMillis(applicationProperties.updateTime.toLong() * 2)
            )
        )
        session.createMutationQuery(delete).executeUpdate()
    }

    @Transactional
    fun count(): Int {
        val session = em.unwrap(Session::class.java)
        val resultList: List<DeploymentUUID> = session
            .createSelectionQuery("select d from DeploymentUUID d", DeploymentUUID::class.java)
            .resultList
        println("resultList: $resultList")
        return resultList.size
    }

}
