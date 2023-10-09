package com.codersergg.taskscheduler.dto

import com.codersergg.taskscheduler.dto.request.ProviderRequest
import java.io.Serializable
import java.net.URI
import java.time.Instant

abstract class RestTask : AbstractTask(), Serializable {
    abstract override val provider: ProviderRequest
    abstract override val createdAt: Instant
    abstract val ulr: URI
    abstract val delay: AbstractDelay
}

data class DefaultRestTask(
    override var provider: ProviderRequest,
    override val createdAt: Instant,
    override val ulr: URI,
    override val delay: AbstractDelay,
) : RestTask(), Serializable