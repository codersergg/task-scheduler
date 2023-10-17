package com.codersergg.taskscheduler.repository

import com.codersergg.taskscheduler.dto.DefaultProvider
import com.codersergg.taskscheduler.dto.response.ProviderResponse
import com.codersergg.taskscheduler.dto.response.ProviderWithTaskResponse
import com.codersergg.taskscheduler.model.Provider
import com.codersergg.taskscheduler.model.Provider_
import com.codersergg.taskscheduler.model.Task_
import com.codersergg.taskscheduler.util.RequestParameters
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.hibernate.Session
import org.hibernate.query.Order
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class ProviderRepository(@PersistenceContext private val em: EntityManager) {

    private val session: Session = em.unwrap(Session::class.java)
    fun getAllProviders(params: RequestParameters): List<ProviderResponse> {
        val resultList = session.createSelectionQuery("SELECT p FROM Provider p", Provider::class.java)
            .setReadOnly(true)
            .setOrder(mutableListOf(Order.asc(Provider_.name)) as List<Order<in Provider>>?)
            .setFirstResult(params.pagination.firstResult)
            .setMaxResults(params.pagination.maxResult)
            .resultList

        return resultList
            .map { it.toProviderResponse() }
    }

    fun getAllProvidersWithTask(params: RequestParameters): List<ProviderWithTaskResponse> {
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
            .map { it.toProviderResponseWithTask() }
    }

    fun getProvider(id: Long): ProviderResponse {
        val find = session.find(Provider::class.java, id)
        return find?.toProviderResponse()
            ?: throw NotFoundException()
    }

    fun getProviderWithTasks(id: Long): ProviderWithTaskResponse {
        val graph = session.createEntityGraph(Provider::class.java)
        graph.addPluralSubgraph(Provider_.tasks).addSubgraph(Task_.provider)
        val load = session.byId(Provider::class.java).withFetchGraph(graph).load(id)
        return load?.toProviderResponseWithTask()
            ?: throw NotFoundException()
    }

    @Transactional
    fun createProvider(providerRequestToAdd: DefaultProvider): ProviderResponse {
        val provider = providerRequestToAdd.toProvider()
        session.persist(provider)
        return provider.toProviderResponse()
    }

    @Transactional
    fun delete(id: Long) {
        val provider = session.byId(Provider::class.java).load(id)
        session.remove(provider)
    }
}

@Repository
interface ProviderRepositoryJpa : JpaRepository<Provider, Long>