package com.codersergg.taskscheduler.dto.request

import com.codersergg.taskscheduler.dto.AbstractDelay
import com.codersergg.taskscheduler.dto.AbstractProvider
import com.codersergg.taskscheduler.dto.AbstractTask
import com.codersergg.taskscheduler.dto.PathResponse
import com.codersergg.taskscheduler.model.Provider
import com.codersergg.taskscheduler.model.Task
import java.io.Serializable
import java.time.Instant

data class TaskToCreateRequest(
    override var provider: ProviderRequest,
    override var createdAt: Instant = Instant.now(),
    override val delay: AbstractDelay,
    override val pathResponse: PathResponse,
    var lastRun: Instant = Instant.EPOCH,
) : AbstractTask(), Serializable, Requestable {
    fun toTask(provider: Provider): Task {
        return Task(
            provider = provider,
            delay = delay,
            pathResponse = pathResponse,
            createdAt = createdAt,
            lastRun = lastRun
        )
    }
}

data class TaskToUpdateRequest(
    val id: Long,
    override var provider: AbstractProvider,
    override var createdAt: Instant = Instant.now(),
    override val delay: AbstractDelay,
    override val pathResponse: PathResponse,
    var lastRun: Instant = Instant.now(),
) : AbstractTask(), Serializable, Requestable