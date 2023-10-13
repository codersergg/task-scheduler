package com.codersergg.taskscheduler.dto

import com.fasterxml.jackson.annotation.JsonRootName
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.io.Serializable

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = DefaultProvider::class, name = "DefaultProvider"),
)
abstract class AbstractProvider : Serializable {
    abstract val name: String
    abstract val type: String
}

@JsonRootName(value = "provider")
data class DefaultProvider(
    override val name: String,
    override val type: String = ProviderType.DEFAULT_PROVIDER.string
) : AbstractProvider(), Serializable

enum class ProviderType(val string: String) {
    DEFAULT_PROVIDER("DefaultProvider"),
}