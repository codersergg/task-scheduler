package com.codersergg.taskscheduler.model

import com.codersergg.taskscheduler.dto.request.OwnerRequest
import com.codersergg.taskscheduler.dto.response.OwnerResponse
import com.codersergg.taskscheduler.dto.response.OwnerWithTaskResponse
import jakarta.persistence.*
import org.hibernate.annotations.NaturalId
import java.io.Serializable
import java.time.LocalDateTime

@Entity
@Table(name = "Owner")
class Owner(
    @Basic(optional = false)
    @Column(name = "name", nullable = false)
    @NaturalId
    var name: String,
    @OneToMany(mappedBy = "owner")
    var tasks: MutableList<Task> = mutableListOf(),
    @Version
    var lastUpdated: LocalDateTime = LocalDateTime.now()
) : BaseEntity<Long>(), Serializable {
    fun toOwnerResponse(): OwnerResponse {
        return OwnerResponse(id!!, name)
    }

    fun toOwnerResponseWithTask(): OwnerWithTaskResponse {
        return OwnerWithTaskResponse(id!!, name, tasks.map { it.toTaskResponseGraph() })
    }

    fun toOwnerRequest(): OwnerRequest {
        return OwnerRequest(id!!, name)
    }
}