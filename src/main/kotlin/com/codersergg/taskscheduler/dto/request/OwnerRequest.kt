package com.codersergg.taskscheduler.dto.request

import com.codersergg.taskscheduler.dto.AbstractOwner
import com.codersergg.taskscheduler.model.Owner
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

data class OwnerRequest(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("name")
    override var name: String,
) : AbstractOwner(), Serializable, Requestable {
    fun toOwner(): Owner {
        return Owner(name)
    }
}

data class OwnerRequestToAdd(
    @JsonProperty("name")
    override var name: String,
) : AbstractOwner(), Serializable, Requestable {
    fun toOwner(): Owner {
        return Owner(name)
    }
}

interface Requestable
