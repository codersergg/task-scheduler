package com.codersergg.taskscheduler.dto

import com.codersergg.taskscheduler.dto.request.Requestable
import com.codersergg.taskscheduler.model.json.AbstractDelay
import com.codersergg.taskscheduler.model.json.PathResponse
import java.io.Serializable
import java.time.Instant

abstract class AbstractTask : Serializable {
    abstract val provider: AbstractProvider
    abstract val createdAt: Instant
    abstract val delay: AbstractDelay
    abstract val pathResponse: PathResponse
}

abstract class AbstractTaskResponse : Serializable {
    abstract val createdAt: Instant
}

data class TaskToUpdateRequest(
    val id: Long,
    override var provider: DefaultProvider,
    override var createdAt: Instant,
    override val delay: AbstractDelay,
    override val pathResponse: PathResponse,
    var lastRun: Instant = Instant.now(),
) : AbstractTask(), Serializable, Requestable