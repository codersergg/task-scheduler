package com.codersergg.taskscheduler.model.json

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.io.Serializable

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = Duration::class, name = "Duration"),
    JsonSubTypes.Type(value = Timer::class, name = "Timer")
)
abstract class AbstractDelay : Serializable {
    abstract val value: Long
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AbstractDelay) return false

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

}

data class Duration(override val value: Long, val type: String = "Duration") : AbstractDelay()
data class Timer(
    override val value: Long,
    val zoneId: Int,
    val hours: Long,
    val minutes: Long,
    val type: String = "Timer"
) : AbstractDelay(), Serializable