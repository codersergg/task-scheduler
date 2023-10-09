package com.codersergg.taskscheduler.service

import com.codersergg.taskscheduler.dto.request.OwnerRequestToAdd
import com.codersergg.taskscheduler.dto.response.OwnerResponse
import com.codersergg.taskscheduler.dto.response.OwnerWithTaskResponse
import com.codersergg.taskscheduler.model.Owner
import com.codersergg.taskscheduler.model.Owner_
import com.codersergg.taskscheduler.model.Task_
import com.codersergg.taskscheduler.repository.OwnerRepository
import com.codersergg.taskscheduler.repository.RequestParameters
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.hibernate.Session
import org.hibernate.query.Order
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class OwnerService(@PersistenceContext private val em: EntityManager, private val ownerRepository: OwnerRepository) {

    fun getAllOwners(params: RequestParameters): List<OwnerResponse> {
        val session = em.unwrap(Session::class.java)

        val resultList = session.createSelectionQuery("SELECT o FROM Owner o", Owner::class.java)
            .setReadOnly(true)
            .setOrder(mutableListOf(Order.asc(Owner_.name)) as List<Order<in Owner>>?)
            .setFirstResult(params.pagination.firstResult)
            .setMaxResults(params.pagination.maxResult)
            .resultList

        return resultList
            .map { it.toOwnerResponse() }
    }

    fun getAllOwnersWithTask(params: RequestParameters): List<OwnerWithTaskResponse> {
        val session = em.unwrap(Session::class.java)
        val ids = session
            .createQuery("SELECT o.id FROM Owner o", Long::class.java)
            .setFirstResult(params.pagination.firstResult)
            .setMaxResults(params.pagination.maxResult)
            .resultList

        val query = session.createSelectionQuery("SELECT o FROM Owner o where o.id in (:ids)", Owner::class.java)
            .setParameterList("ids", ids)
            .setReadOnly(true)
            .setOrder(mutableListOf(Order.asc(Owner_.name)) as List<Order<in Owner>>?)

        val graph = session.createEntityGraph(Owner::class.java)
        graph.addPluralSubgraph(Owner_.tasks).addSubgraph(Task_.owner)
        val resultList = query.setHint("jakarta.persistence.fetchgraph", graph)

        return resultList.resultList
            .map { it.toOwnerResponseWithTask() }
    }

    fun getOwner(id: Long): OwnerResponse {
        val session = em.unwrap(Session::class.java)
        val find = session.find(Owner::class.java, id)
        return find?.toOwnerResponse()
            ?: throw NotFoundException()
    }

    fun getOwnerWithTasks(id: Long): OwnerWithTaskResponse {
        val session = em.unwrap(Session::class.java)
        val graph = session.createEntityGraph(Owner::class.java)
        graph.addPluralSubgraph(Owner_.tasks).addSubgraph(Task_.owner)
        val load = session.byId(Owner::class.java).withFetchGraph(graph).load(id)
        return load?.toOwnerResponseWithTask()
            ?: throw NotFoundException()
    }

    fun createOwner(ownerRequestToAdd: OwnerRequestToAdd): OwnerResponse {
        throw Exception()
    }
}