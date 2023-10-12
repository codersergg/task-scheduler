package com.codersergg.taskscheduler.dto.request

import com.codersergg.taskscheduler.dto.AbstractProvider
import com.codersergg.taskscheduler.model.Provider
import java.io.Serializable

data class ProviderRequest(
    val id: Long,
    override var name: String,
) : AbstractProvider(), Serializable, Requestable

data class ProviderRequestToAdd(
    override var name: String,
) : AbstractProvider(), Serializable, Requestable {
    fun toProvider(): Provider {
        return Provider(name)
    }
}

interface Requestable
