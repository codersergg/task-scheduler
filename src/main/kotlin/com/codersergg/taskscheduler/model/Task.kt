package com.codersergg.taskscheduler.model

import com.codersergg.taskscheduler.dto.response.TaskResponseWithDelay
import com.codersergg.taskscheduler.model.json.AbstractDelay
import com.codersergg.taskscheduler.model.json.PathResponse
import com.fasterxml.jackson.annotation.JsonTypeInfo
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Entity
@Table(name = "Task")
class Task(
    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "providerId")
    var provider: Provider,
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
    @Column(name = "delay", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    var delay: AbstractDelay,
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
    @Column(name = "pathResponse", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    var pathResponse: PathResponse,
    @Column(name = "createdAt", nullable = false)
    var createdAt: Instant = Instant.now(),
    @Column(name = "lastRun", nullable = false)
    var lastRun: Instant = Instant.EPOCH,
) : BaseEntity<Long>() {

    fun toTaskResponseWithDelay(): TaskResponseWithDelay {
        return TaskResponseWithDelay(
            id = id!!,
            provider = provider.toProviderResponse(),
            delay = delay,
            pathResponse = pathResponse,
            lastRun = convertInstantToString(lastRun),
            createdAt = createdAt,
        )
    }

    private fun convertInstantToString(instant: Instant): String {
        val format = "dd.MM.yyyy hh:mm:ss"
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(format)
            .withZone(ZoneId.systemDefault())
        return formatter.format(instant)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as Task

        if (provider != other.provider) return false
        if (createdAt != other.createdAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + provider.hashCode()
        result = 31 * result + createdAt.hashCode()
        return result
    }
}