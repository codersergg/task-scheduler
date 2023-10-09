package com.codersergg.taskscheduler.dto

import java.io.Serializable
import java.time.Duration

abstract class AbstractDelay : Serializable

data class DefaultDelay(
    val duration: Duration
) : AbstractDelay(), Serializable