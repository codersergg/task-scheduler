package com.codersergg.taskscheduler.service

import com.codersergg.taskscheduler.model.*
import com.codersergg.taskscheduler.repository.EntityManagerFactoryService
import com.codersergg.taskscheduler.repository.RequestParameters
import org.hibernate.query.Order
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.stereotype.Service

@Service
class OwnerService(private val emf: EntityManagerFactoryService) {
    fun getAllOwners(params: RequestParameters): List<OwnerResponse> {
        return emf.session().createSelectionQuery("SELECT o FROM Owner o", Owner::class.java)
            .setReadOnly(true)
            .setOrder(mutableListOf(Order.asc(Owner_.name)) as List<Order<in Owner>>?)
            .setMaxResults(params.pagination.maxResult)
            .setFirstResult(params.pagination.firstResult)
            .resultList
            .map { it.toOwnerResponse() }
    }

    fun getAllOwnersWithTask(params: RequestParameters): List<OwnerResponseWithTask> {
        val graph = emf.session().createEntityGraph(Owner::class.java)
        graph.addPluralSubgraph(Owner_.tasks).addSubgraph(Task_.owner)
        val query = emf.session().createSelectionQuery("SELECT o FROM Owner o", Owner::class.java)
            .setReadOnly(true)
            .setOrder(mutableListOf(Order.asc(Owner_.name)) as List<Order<in Owner>>?)
            .setMaxResults(params.pagination.maxResult)
            .setFirstResult(params.pagination.firstResult)
        return query.setHint("jakarta.persistence.fetchgraph", graph)
            .resultList
            .map { it.toOwnerResponseWithTask() }
    }

    fun getOwner(id: Long): OwnerResponse {
        return emf.session().find(Owner::class.java, id)?.toOwnerResponse()
            ?: throw NotFoundException()
    }

    fun getOwnerWithTasks(id: Long): OwnerResponseWithTask {
        val graph = emf.session().createEntityGraph(Owner::class.java)
        graph.addPluralSubgraph(Owner_.tasks).addSubgraph(Task_.owner)
        return emf.session().byId(Owner::class.java).withFetchGraph(graph).load(id)?.toOwnerResponseWithTask()
            ?: throw NotFoundException()
    }

    fun createOwner(ownerRequestToAdd: OwnerRequestToAdd): OwnerResponse {
        throw Exception()
    }
}