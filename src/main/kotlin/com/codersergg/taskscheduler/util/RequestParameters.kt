package com.codersergg.taskscheduler.util

import java.io.Serializable

abstract class AbstractRequestParameters(
    open val pagination: Pagination = Pagination()
)

data class RequestParameters(
    override val pagination: Pagination = Pagination(),
) : AbstractRequestParameters(), Serializable

data class Pagination(
    val firstResult: Int = 0,
    val maxResult: Int = 20
) : Serializable