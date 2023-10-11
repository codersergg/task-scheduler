package com.codersergg.taskscheduler.dto.response

import com.codersergg.taskscheduler.dto.AbstractDelay
import com.codersergg.taskscheduler.dto.AbstractTask
import com.codersergg.taskscheduler.dto.AbstractTaskResponse
import java.io.Serializable
import java.time.Instant

data class TaskResponse(
    val id: Long,
    override val createdAt: Instant,
    var lastRun: String,
) : AbstractTaskResponse(), Serializable, Responsible

data class TaskResponseWithTask(
    val id: Long,
    override val provider: ProviderResponse,
    override val createdAt: Instant,
    override val delay: AbstractDelay,
    var lastRun: String,
) : AbstractTask(), Serializable, Responsible