package com.codersergg.taskscheduler.model

import com.codersergg.taskscheduler.dto.AbstractDelay
import com.codersergg.taskscheduler.dto.request.TaskToUpdateRequest
import com.codersergg.taskscheduler.dto.response.TaskResponse
import com.codersergg.taskscheduler.dto.response.TaskResponseWithDelay
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Entity
@Table(name = "Task")
class Task(
    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "providerId")
    var provider: Provider,
    @Column(name = "createdAt", nullable = false)
    var createdAt: Instant,
    @Column(name = "lastRun", nullable = false)
    var lastRun: Instant,
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
    @Column(name = "delay", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    var delay: AbstractDelay,
    @Version
    var lastUpdated: LocalDateTime = LocalDateTime.now()
) : BaseEntity<Long>() {

    fun toTaskResponseWithDelay(): TaskResponseWithDelay {
        return TaskResponseWithDelay(
            id!!,
            provider.toProviderResponse(),
            createdAt,
            delay,
            convertInstantToString(lastRun)
        )
    }

    fun toTaskRequestWithDelay(): TaskToUpdateRequest {
        return TaskToUpdateRequest(
            id!!,
            provider.toProviderResponse(),
            createdAt,
            delay
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