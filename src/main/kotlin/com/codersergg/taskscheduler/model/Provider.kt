package com.codersergg.taskscheduler.model

import com.codersergg.taskscheduler.dto.request.ProviderRequest
import com.codersergg.taskscheduler.dto.response.ProviderResponse
import com.codersergg.taskscheduler.dto.response.ProviderWithTaskResponse
import jakarta.persistence.*
import org.hibernate.annotations.NaturalId
import java.io.Serializable
import java.time.LocalDateTime

@Entity
@Table(name = "Provider")
class Provider(
    @Basic(optional = false)
    @Column(name = "name", nullable = false)
    @NaturalId
    var name: String,
    @OneToMany(mappedBy = "provider")
    var tasks: MutableList<Task> = mutableListOf(),
    @Version
    var lastUpdated: LocalDateTime = LocalDateTime.now()
) : BaseEntity<Long>(), Serializable {
    fun toProviderResponse(): ProviderResponse {
        return ProviderResponse(id!!, name)
    }

    fun toProviderResponseWithTask(): ProviderWithTaskResponse {
        return ProviderWithTaskResponse(id!!, name, tasks.map { it.toTaskResponseGraph() })
    }

    fun toOwnerRequest(): ProviderRequest {
        return ProviderRequest(id!!, name)
    }
}