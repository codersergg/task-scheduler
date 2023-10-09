package com.codersergg.taskscheduler.repository

import com.codersergg.taskscheduler.model.Provider
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProviderRepository : JpaRepository<Provider, Long> {

    @EntityGraph(
        type = EntityGraph.EntityGraphType.FETCH,
        attributePaths = ["id", "name", "task"]
    )
    fun findAllByOrderById(): List<Provider>
}
