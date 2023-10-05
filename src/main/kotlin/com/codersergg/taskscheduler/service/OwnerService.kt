package com.codersergg.taskscheduler.service

import com.codersergg.taskscheduler.model.*
import jakarta.persistence.EntityManagerFactory
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.stereotype.Service


@Service
class OwnerService(private val factory: EntityManagerFactory) {
    fun session(): Session {
        factory as SessionFactory
        return factory.openSession()
    }

    fun entityManagerFactory(): EntityManagerFactory {
        return factory
    }

    fun getAllOwners(): List<OwnerResponse> {
        return session().createSelectionQuery("SELECT o FROM Owner o", Owner::class.java)
            .resultList
            .map { it.toOwnerResponse() }
    }

    fun getAllOwnersWithTask(): List<OwnerResponseWithTask> {
        val graph = session().createEntityGraph(Owner::class.java)
        graph.addPluralSubgraph(Owner_.tasks).addSubgraph(Task_.owner)
        val query = session().createSelectionQuery("SELECT o FROM Owner o", Owner::class.java)
        return query.setHint("javax.persistence.fetchgraph", graph)
            .resultList
            .map { it.toOwnerResponseWithTask() }
    }

    fun getOwner(id: Long): OwnerResponse {
        return session().find(Owner::class.java, id)?.toOwnerResponse()
            ?: throw NotFoundException()
    }

    fun getOwnerWithTasks(id: Long): OwnerResponseWithTask {
        val graph = session().createEntityGraph(Owner::class.java)
        graph.addPluralSubgraph(Owner_.tasks).addSubgraph(Task_.owner)
        return session().byId(Owner::class.java).withFetchGraph(graph).load(id)?.toOwnerResponseWithTask()
            ?: throw NotFoundException()
    }
}