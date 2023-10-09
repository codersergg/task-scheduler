package com.codersergg.taskscheduler.dto.response

import com.codersergg.taskscheduler.dto.AbstractProvider
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

data class ProviderResponse(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("name")
    override var name: String,
) : AbstractProvider(), Serializable, Responsible

data class ProviderWithTaskResponse(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("name")
    override var name: String,
    val tasks: List<TaskResponseWithTask>
) : AbstractProvider(), Serializable, Responsible

interface Responsible