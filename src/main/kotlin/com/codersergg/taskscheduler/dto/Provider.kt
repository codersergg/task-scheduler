package com.codersergg.taskscheduler.dto

import com.codersergg.taskscheduler.dto.request.TaskToCreateRequest
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.io.Serializable

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = DefaultProvider::class, name = "DefaultProvider"),
)
abstract class AbstractProvider : Serializable {
    abstract val name: String
}

data class DefaultProvider(
    override val name: String,
    val type: String = "DefaultProvider"
) : AbstractProvider(), Serializable