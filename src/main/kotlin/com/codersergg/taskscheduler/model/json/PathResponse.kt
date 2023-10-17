package com.codersergg.taskscheduler.model.json

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.net.URI

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = RestPathResponse::class, name = "RestPathResponse"),
)
abstract class PathResponse {
    abstract val path: Any
    abstract val type: String
}

data class RestPathResponse(
    override val path: URI,
    override val type: String = "RestPathResponse"
) : PathResponse()