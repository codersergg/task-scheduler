package com.codersergg.taskscheduler.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import java.io.Serializable
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@Entity
@Table(name = "Task")
class Task(
    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    var owner: Owner,
    @Column(name = "last_run", nullable = false)
    var lastRun: Instant,
    @Version
    var lastUpdated: LocalDateTime = LocalDateTime.now()
) : BaseEntity<Long>() {

    fun toTaskResponse(): TaskResponse {
        return TaskResponse(id!!, owner.toOwnerResponseLazy(), convertInstantToString(lastRun))
    }

    fun toTaskResponseGraph(): TaskResponseGraph {
        return TaskResponseGraph(id!!, convertInstantToString(lastRun))
    }

    private fun convertInstantToString(instant: Instant): String {
        val format = "dd.MM.yyyy hh:mm:ss"
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(format)
            .withZone(ZoneId.systemDefault())
        return formatter.format(instant)
    }
}

abstract class AbstractTask : Serializable {
    abstract var owner: OwnerRequest
}

data class TaskRequestToCreate(
    @JsonProperty("owner")
    override var owner: OwnerRequest,
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

data class TaskRequestToUpdate(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("owner")
    override var owner: OwnerRequest,
    @JsonProperty("last_run")
    var lastRun: Instant = Instant.now(),
) : AbstractTask(), Serializable

data class TaskResponse(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("owner")
    var owner: OwnerResponseLazy,
    @JsonProperty("last_run")
    var lastRun: String,
) : Serializable

data class TaskResponseGraph(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("last_run")
    var lastRun: String,
) : Serializable
