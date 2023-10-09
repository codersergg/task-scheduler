package com.codersergg.taskscheduler.dto.request

import com.codersergg.taskscheduler.dto.AbstractTask
import com.codersergg.taskscheduler.model.Owner
import com.codersergg.taskscheduler.model.Task
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.time.Instant

data class TaskToCreateRequest(
    @JsonProperty("owner")
    var owner: OwnerRequest,
    @JsonProperty("last_run")
    var lastRun: Instant = Instant.EPOCH,
) : AbstractTask(), Serializable {
    fun toTask(): Task {
        return Task(owner.toOwner(), lastRun)
    }

    fun toTask(owner: Owner): Task {
        return Task(owner, lastRun)
    }
}

data class TaskToUpdateRequest(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("owner")
    var owner: OwnerRequest,
    @JsonProperty("last_run")
    var lastRun: Instant = Instant.now(),
) : AbstractTask(), Serializable