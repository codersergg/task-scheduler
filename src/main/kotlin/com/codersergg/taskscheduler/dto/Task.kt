package com.codersergg.taskscheduler.dto

import java.io.Serializable
import java.time.Instant

abstract class AbstractTask : Serializable {
    abstract val provider: AbstractProvider
    abstract val createdAt: Instant
}

abstract class AbstractTaskResponse : Serializable {
    abstract val createdAt: Instant
}