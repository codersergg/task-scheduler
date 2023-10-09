package com.codersergg.taskscheduler.dto.request

import com.codersergg.taskscheduler.dto.AbstractProvider
import com.codersergg.taskscheduler.model.Provider
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

data class ProviderRequest(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("name")
    override var name: String,
) : AbstractProvider(), Serializable, Requestable {
    fun toOwner(): Provider {
        return Provider(name)
    }
}

data class ProviderRequestToAdd(
    @JsonProperty("name")
    override var name: String,
) : AbstractProvider(), Serializable, Requestable {
    fun toProvider(): Provider {
        return Provider(name)
    }
}

interface Requestable
