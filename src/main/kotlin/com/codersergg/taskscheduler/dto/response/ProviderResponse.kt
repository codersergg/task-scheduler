package com.codersergg.taskscheduler.dto.response

import com.codersergg.taskscheduler.dto.AbstractProvider
import java.io.Serializable

data class ProviderResponse(
    val id: Long,
    override var name: String,
) : AbstractProvider(), Serializable, Responsible

data class ProviderWithTaskResponse(
    val id: Long,
    override var name: String,
    val tasks: List<TaskResponseWithDelay>
) : AbstractProvider(), Serializable, Responsible

interface Responsible