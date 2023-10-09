package com.codersergg.taskscheduler.repository

import com.codersergg.taskscheduler.dto.request.Requestable
import java.io.Serializable

abstract class AbstractRequestParameters(
    open val pagination: Pagination = Pagination()
)

data class RequestParameters(
    override val pagination: Pagination = Pagination(),
) : AbstractRequestParameters(), Serializable

data class RequestParametersEntity<T>(
    override val pagination: Pagination = Pagination(),
    val entity: T? = null
) : AbstractRequestParameters(), Serializable where T : Requestable

data class Pagination(
    val firstResult: Int = 0,
    val maxResult: Int = 20
) : Serializable


