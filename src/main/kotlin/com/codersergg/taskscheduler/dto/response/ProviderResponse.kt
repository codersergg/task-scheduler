package com.codersergg.taskscheduler.dto.response

import com.codersergg.taskscheduler.dto.AbstractProvider
import java.io.Serializable

data class ProviderResponse(
    val id: Long,
    override var name: String,
    override val type: String,
) : AbstractProvider(), Serializable

data class ProviderWithTaskResponse(
    val id: Long,
    override var name: String,
    override val type: String,
    val tasks: List<TaskResponseWithDelay>,
) : AbstractProvider(), Serializable