package com.codersergg.taskscheduler.dto.request

import com.codersergg.taskscheduler.dto.AbstractTask
import com.codersergg.taskscheduler.model.Provider
import com.codersergg.taskscheduler.model.Task
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.time.Instant

data class TaskToCreateRequest(
    @JsonProperty("owner")
    var provider: ProviderRequest,
    @JsonProperty("last_run")
    var lastRun: Instant = Instant.EPOCH,
) : AbstractTask(), Serializable {
    fun toTask(): Task {
        return Task(provider.toOwner(), lastRun)
    }

    fun toTask(provider: Provider): Task {
        return Task(provider, lastRun)
    }
}

data class TaskToUpdateRequest(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("owner")
    var owner: ProviderRequest,
    @JsonProperty("last_run")
    var lastRun: Instant = Instant.now(),
) : AbstractTask(), Serializable