package com.codersergg.taskscheduler.dto

import com.codersergg.taskscheduler.model.json.AbstractDelay
import com.codersergg.taskscheduler.model.json.PathResponse
import java.io.Serializable
import java.time.Instant

abstract class AbstractTask : Serializable {
    abstract var id: Long?
    abstract val provider: AbstractProvider
    abstract val createdAt: Instant
    abstract val delay: AbstractDelay
    abstract val pathResponse: PathResponse
}