package com.codersergg.taskscheduler.repository

import com.codersergg.taskscheduler.model.Task
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.sql.Timestamp
import java.time.Instant

@Repository
interface TaskRepository : JpaRepository<Task, Long> {
    fun findAllByOrderById(): List<Task>

    @Transactional
    @Modifying
    @Query("update Task t set t.lastRun = ?2 where t.id = ?1")
    fun update(id: Long, lastRun: Instant): Int
}