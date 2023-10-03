package com.codersergg.taskscheduler.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import java.io.Serializable
import java.sql.Timestamp
import java.time.Instant

@Entity
@Table(name = "tasks")
class Task(
    @JsonProperty("task_name")
    @Column(name = "task_name", nullable = false)
    var taskName: String,
    @JsonProperty("last_run")
    @Column(name = "last_run", nullable = false)
    var lastRun: Timestamp,
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    var id: Long? = null
) {
    fun toTaskResponse(): TaskResponse {
        return TaskResponse(id!!, taskName, lastRun)
    }
}

data class TaskRequestToCreate(
    @JsonProperty("task_name")
    var taskName: String,
    @JsonProperty("last_run")
    var lastRun: Timestamp = Timestamp.from(Instant.EPOCH),
) : Serializable {
    fun toTask(): Task {
        return Task(taskName, lastRun)
    }
}

data class TaskRequestToUpdate(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("task_name")
    var taskName: String,
    @JsonProperty("last_run")
    var lastRun: Timestamp = Timestamp.from(Instant.now()),
) : Serializable {
    fun toTask(): Task {
        return Task(taskName, lastRun)
    }
}

data class TaskResponse(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("task_name")
    var taskName: String,
    @JsonProperty("last_run")
    var lastRun: Timestamp,
) : Serializable
