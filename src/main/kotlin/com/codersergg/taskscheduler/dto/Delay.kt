package com.codersergg.taskscheduler.dto

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.io.Serializable

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = Duration::class, name = "Duration"),
    JsonSubTypes.Type(value = Timer::class, name = "Timer")
)
abstract class AbstractDelay : Serializable {
    abstract val value: Any
}

data class Duration(override val value: Long, val type: String = "Duration") : AbstractDelay()
data class Timer(
    override val value: Long,
    val zoneId: Int,
    val hours: Long,
    val minutes: Long,
    val delayInDays: Long,
    val type: String = "Timer"
) : AbstractDelay(), Serializable