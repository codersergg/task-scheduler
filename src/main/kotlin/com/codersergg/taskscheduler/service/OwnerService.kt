package com.codersergg.taskscheduler.service

import com.codersergg.taskscheduler.model.*
import com.codersergg.taskscheduler.repository.OwnerRepository
import jakarta.persistence.EntityManagerFactory
import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.stereotype.Service


@Service
class OwnerService(
    @Autowired private val ownerRepository: OwnerRepository,
    @Autowired private val factory: EntityManagerFactory
) {

    fun sessionFactory(): SessionFactory {
        return factory as SessionFactory
    }

    fun entityManagerFactory(): EntityManagerFactory {
        return factory
    }

    fun getOwner(id: Long): OwnerResponseLazy {
        return sessionFactory().openSession().find(Owner::class.java, id)?.toOwnerResponseLazy()
            ?: throw NotFoundException()
    }

    fun getOwnerWithTasks(id: Long): OwnerResponseGraph {
        val createEntityGraph = sessionFactory().openSession()
            .createEntityGraph(Owner::class.java)

        return createEntityGraph
            .addSubgraph(Owner_.task)

            .find(Owner::class.java, id)?.toOwnerResponseLazy()
            ?: throw NotFoundException()
    }

    fun getAllOwners(): List<OwnerResponse> {
        return listOf()
    }

    fun getAllOwnersGraph(): List<OwnerResponseGraph> {
        return ownerRepository.findAllByOrderById().map { it.toOwnerResponseGraph() }
    }
}