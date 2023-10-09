package com.codersergg.taskscheduler.dto.response

import com.codersergg.taskscheduler.dto.AbstractTask
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

data class TaskResponse(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("owner")
    var owner: ProviderResponse,
    @JsonProperty("last_run")
    var lastRun: String,
) : AbstractTask(), Serializable

data class TaskResponseWithTask(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("last_run")
    var lastRun: String,
) : AbstractTask(), Serializable