package com.codersergg.taskscheduler.model

import com.codersergg.taskscheduler.dto.response.TaskResponse
import com.codersergg.taskscheduler.dto.response.TaskResponseWithTask
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Entity
@Table(name = "Task")
class Task(
    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id")
    var provider: Provider,
    @Column(name = "last_run", nullable = false)
    var lastRun: Instant,
    @Version
    var lastUpdated: LocalDateTime = LocalDateTime.now()
) : BaseEntity<Long>() {

    fun toTaskResponse(): TaskResponse {
        return TaskResponse(id!!, provider.toProviderResponse(), convertInstantToString(lastRun))
    }

    fun toTaskResponseGraph(): TaskResponseWithTask {
        return TaskResponseWithTask(id!!, convertInstantToString(lastRun))
    }

    private fun convertInstantToString(instant: Instant): String {
        val format = "dd.MM.yyyy hh:mm:ss"
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(format)
            .withZone(ZoneId.systemDefault())
        return formatter.format(instant)
    }
}
