package com.codersergg.taskscheduler.model

import com.codersergg.taskscheduler.dto.response.ProviderResponse
import com.codersergg.taskscheduler.dto.response.ProviderWithTaskResponse
import jakarta.persistence.*
import org.hibernate.annotations.NaturalId
import java.io.Serializable

@Entity
@Table(name = "Provider")
class Provider(
    @Basic(optional = false)
    @Column(name = "name", nullable = false)
    @NaturalId
    var name: String,
    @Column(name = "type", nullable = false)
    var type: String,
    @OneToMany(mappedBy = "provider")
    var tasks: MutableList<Task> = mutableListOf(),
) : BaseEntity<Long>(), Serializable {
    constructor(id: Long, name: String, type: String) : this(
        name = name,
        type = type
    ) {
        super.id = id
    }

    fun toProviderResponse(): ProviderResponse {
        return ProviderResponse(id!!, name, type)
    }

    fun toProviderResponseWithTask(): ProviderWithTaskResponse {
        return ProviderWithTaskResponse(id!!, name, type, tasks.map { it.toTaskResponseWithDelay() })
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as Provider

        return name == other.name
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}