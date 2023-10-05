package com.codersergg.taskscheduler.repository

import com.codersergg.taskscheduler.model.Owner
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OwnerRepository : JpaRepository<Owner, Long> {

    @EntityGraph(
        type = EntityGraph.EntityGraphType.FETCH,
        attributePaths = ["id", "name", "task"]
    )
    fun findAllByOrderById(): List<Owner>
}
