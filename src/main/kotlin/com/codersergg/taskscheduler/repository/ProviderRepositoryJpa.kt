package com.codersergg.taskscheduler.repository

import com.codersergg.taskscheduler.model.Provider
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProviderRepositoryJpa : JpaRepository<Provider, Long> {

    @EntityGraph(
        type = EntityGraph.EntityGraphType.FETCH,
        attributePaths = ["id", "name", "task"]
    )
    fun findByName(name: String): Optional<Provider>
}