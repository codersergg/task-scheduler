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
    abstract val type: String
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AbstractDelay) return false

        if (value != other.value) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = value.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }

}

data class Duration(
    override val value: Long,
    override val type: String = DelayType.DURATION.string
) : AbstractDelay()

data class Timer(
    override val value: Long,
    val zoneId: Int,
    val hours: Long,
    val minutes: Long,
    override val type: String = DelayType.TIMER.string
) : AbstractDelay(), Serializable

enum class DelayType(val string: String) {
    DURATION("Duration"),
    TIMER("Timer"),
}