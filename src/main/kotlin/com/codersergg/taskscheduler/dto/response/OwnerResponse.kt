package com.codersergg.taskscheduler.dto.response

import com.codersergg.taskscheduler.dto.AbstractOwner
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

data class OwnerResponse(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("name")
    override var name: String,
) : AbstractOwner(), Serializable, Responsible

data class OwnerWithTaskResponse(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("name")
    override var name: String,
    val tasks: List<TaskResponseWithTask>
) : AbstractOwner(), Serializable, Responsible

interface Responsible