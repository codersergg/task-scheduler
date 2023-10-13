package com.codersergg.taskscheduler.dto.response

import com.codersergg.taskscheduler.dto.AbstractTask
import com.codersergg.taskscheduler.model.json.AbstractDelay
import com.codersergg.taskscheduler.model.json.PathResponse
import java.io.Serializable
import java.time.Instant

data class TaskResponseWithDelay(
    val id: Long,
    override val provider: ProviderResponse,
    override val createdAt: Instant,
    override val delay: AbstractDelay,
    override val pathResponse: PathResponse,
    var lastRun: String,
) : AbstractTask(), Serializable, Responsible