package com.codersergg.taskscheduler.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.Instant
import java.util.*

@Entity
@Table(name = "AppInitializer")
class AppInitializer(
    @Column(name = "uuid", unique = true, nullable = false)
    var uuid: UUID,
    @Column(name = "lastActivity", nullable = false)
    var lastActivity: Instant,
) : BaseEntity<Long>()