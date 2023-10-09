package com.codersergg.taskscheduler.service

import com.codersergg.taskscheduler.dto.request.ProviderRequestToAdd
import com.codersergg.taskscheduler.dto.response.ProviderResponse
import com.codersergg.taskscheduler.dto.response.ProviderWithTaskResponse
import com.codersergg.taskscheduler.model.Provider_
import com.codersergg.taskscheduler.model.Provider
import com.codersergg.taskscheduler.model.Task_
import com.codersergg.taskscheduler.repository.ProviderRepository
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
class ProviderService(
    @PersistenceContext private val em: EntityManager,
    private val providerRepository: ProviderRepository
) {

    fun getAllOwners(params: RequestParameters): List<ProviderResponse> {
        val session = em.unwrap(Session::class.java)

        val resultList = session.createSelectionQuery("SELECT p FROM Provider p", Provider::class.java)
            .setReadOnly(true)
            .setOrder(mutableListOf(Order.asc(Provider_.name)) as List<Order<in Provider>>?)
            .setFirstResult(params.pagination.firstResult)
            .setMaxResults(params.pagination.maxResult)
            .resultList

        return resultList
            .map { it.toProviderResponse() }
    }

    fun getAllOwnersWithTask(params: RequestParameters): List<ProviderWithTaskResponse> {
        val session = em.unwrap(Session::class.java)
        val ids = session
            .createQuery("SELECT p.id FROM Provider p", Long::class.java)
            .setFirstResult(params.pagination.firstResult)
            .setMaxResults(params.pagination.maxResult)
            .resultList

        val query = session.createSelectionQuery("SELECT p FROM Provider p where p.id in (:ids)", Provider::class.java)
            .setParameterList("ids", ids)
            .setReadOnly(true)
            .setOrder(mutableListOf(Order.asc(Provider_.name)) as List<Order<in Provider>>?)

        val graph = session.createEntityGraph(Provider::class.java)
        graph.addPluralSubgraph(Provider_.tasks).addSubgraph(Task_.provider)
        val resultList = query.setHint("jakarta.persistence.fetchgraph", graph)

        return resultList.resultList
            .map { it.toOwnerResponseWithTask() }
    }

    fun getOwner(id: Long): ProviderResponse {
        val session = em.unwrap(Session::class.java)
        val find = session.find(Provider::class.java, id)
        return find?.toProviderResponse()
            ?: throw NotFoundException()
    }

    fun getOwnerWithTasks(id: Long): ProviderWithTaskResponse {
        val session = em.unwrap(Session::class.java)
        val graph = session.createEntityGraph(Provider::class.java)
        graph.addPluralSubgraph(Provider_.tasks).addSubgraph(Task_.provider)
        val load = session.byId(Provider::class.java).withFetchGraph(graph).load(id)
        return load?.toOwnerResponseWithTask()
            ?: throw NotFoundException()
    }

    fun createOwner(ownerRequestToAdd: ProviderRequestToAdd): ProviderResponse {
        throw Exception()
    }
}