package com.codersergg.taskscheduler.dto.request

import com.codersergg.taskscheduler.dto.AbstractTask
import com.codersergg.taskscheduler.model.Provider
import com.codersergg.taskscheduler.model.Task
import java.io.Serializable
import java.time.Instant

data class TaskToCreateRequest(
    override var provider: ProviderRequest,
    override var createdAt: Instant = Instant.now(),
    var lastRun: Instant = Instant.EPOCH,
) : AbstractTask(), Serializable, Requestable {
    fun toTask(): Task {
        return Task(provider.toProvider(), createdAt, lastRun)
    }

    fun toTask(provider: Provider): Task {
        return Task(provider, createdAt, lastRun)
    }
}

data class TaskToUpdateRequest(
    val id: Long,
    override var provider: ProviderRequest,
    override var createdAt: Instant = Instant.now(),
    var lastRun: Instant = Instant.now(),
) : AbstractTask(), Serializable, Requestable