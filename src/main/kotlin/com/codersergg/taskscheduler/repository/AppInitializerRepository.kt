package com.codersergg.taskscheduler.repository

import com.codersergg.taskscheduler.configuration.ApplicationProperties
import com.codersergg.taskscheduler.model.AppInitializer
import com.codersergg.taskscheduler.model.AppInitializer_
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.persistence.criteria.CriteriaDelete
import jakarta.persistence.criteria.CriteriaUpdate
import org.hibernate.Session
import org.hibernate.query.criteria.HibernateCriteriaBuilder
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.time.Instant
import java.util.*


@Repository
class AppInitializerRepository(
    @PersistenceContext private val em: EntityManager,
    private val applicationProperties: ApplicationProperties,
) {
    private val session: Session = em.unwrap(Session::class.java)
    private val uuid: UUID = uuidFromInstant
    private val logger = KotlinLogging.logger {}

    companion object UUIDGenerator {
        private val now: Instant = Instant.now()
        private val bytes: ByteArray = ByteArrayOutputStream().use { stream ->
            DataOutputStream(stream).use { dataStream ->
                dataStream.writeLong(now.toEpochMilli())
                stream.toByteArray()
            }
        }
        val uuidFromInstant: UUID = UUID.nameUUIDFromBytes(bytes)
    }

    @Transactional
    fun register(): Boolean {
        val appInitializer = AppInitializer(uuid, Instant.now())
        session.persist(appInitializer)
        session.flush()
        session.clear()
        return appInitializer.id != null
    }

    @Transactional
    fun updateLastActivity(): Int {
        val builder: HibernateCriteriaBuilder = session.criteriaBuilder
        val update: CriteriaUpdate<AppInitializer> = builder.createCriteriaUpdate(AppInitializer::class.java)
        val appInitializerRoot = update.from(AppInitializer::class.java)
        update.where(builder.equal(appInitializerRoot[AppInitializer_.uuid], uuid))
        update.set(AppInitializer_.lastActivity, Instant.now())
        val executeUpdate = session.createMutationQuery(update).executeUpdate()
        session.flush()
        session.clear()
        logger.info { "Update AppInitializer.lastActivity" }
        return executeUpdate
    }

    fun getCurrent(): AppInitializer {
        return getAppInitializer(uuid)
    }

    fun getByUUID(uuid: UUID): AppInitializer {
        return getAppInitializer(uuid)
    }

    private fun getAppInitializer(uuid: UUID): AppInitializer {
        val criteriaBuilder = session.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(AppInitializer::class.java)
        val root = criteriaQuery.from(AppInitializer::class.java)
        val uuidPath = root.get<UUID>(AppInitializer_.UUID)
        criteriaQuery.where(criteriaBuilder.equal(uuidPath, uuid))
        val query = session.createQuery(criteriaQuery)
        return query.singleResult
    }

    @Transactional
    fun delete() {
        val session = em.unwrap(Session::class.java)
        val builder: HibernateCriteriaBuilder = session.criteriaBuilder
        val delete: CriteriaDelete<AppInitializer> = builder.createCriteriaDelete(AppInitializer::class.java)
        val appInitializerRoot = delete.from(AppInitializer::class.java)
        delete.where(
            builder.lessThan(
                appInitializerRoot[AppInitializer_.lastActivity],
                Instant.now().minusMillis(applicationProperties.updateTime.toLong() * 2)
            )
        )
        session.createMutationQuery(delete).executeUpdate()
    }

    @Transactional
    fun count(): Int {
        val session = em.unwrap(Session::class.java)
        val resultList: List<AppInitializer> = session
            .createSelectionQuery("select d from AppInitializer d", AppInitializer::class.java)
            .resultList
        println("resultList: $resultList")
        return resultList.size
    }

}
