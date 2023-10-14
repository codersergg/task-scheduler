package com.codersergg.taskscheduler.dto

import com.codersergg.taskscheduler.model.Provider
import com.codersergg.taskscheduler.model.Task
import com.codersergg.taskscheduler.model.json.*
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.io.Serializable
import java.time.Instant

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = DurationRestTask::class, name = "DurationRestTask"),
    JsonSubTypes.Type(value = TimerRestTask::class, name = "TimerRestTask")
)
abstract class RestTask : AbstractTask(), Serializable {
    abstract override val provider: AbstractProvider
    abstract override val createdAt: Instant
    abstract override val delay: AbstractDelay
    abstract override val pathResponse: PathResponse
    abstract val type: String

    fun toTask() = Task(
        Provider(name = this.provider.name, type = this.provider.type),
        createdAt = this.createdAt,
        delay = this.delay,
        pathResponse = this.pathResponse
    )

}

data class DurationRestTask(
    override val provider: DefaultProvider,
    override val createdAt: Instant = Instant.now(),
    override var delay: Duration,
    override val pathResponse: RestPathResponse,
    override val type: String = "DurationRestTask"
) : RestTask(), Serializable

data class TimerRestTask(
    override val provider: DefaultProvider,
    override val createdAt: Instant = Instant.now(),
    override var delay: Timer,
    override val pathResponse: RestPathResponse,
    override val type: String = "TimerRestTask"
) : RestTask(), Serializable