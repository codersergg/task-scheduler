package com.codersergg.taskscheduler.model

import com.fasterxml.jackson.annotation.JsonProperty
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
    var task: MutableList<Task> = mutableListOf(),
    @Version
    var lastUpdated: LocalDateTime = LocalDateTime.now()
) : BaseEntity<Long>(), Serializable {
    fun toOwnerResponse(): OwnerResponse {
        return OwnerResponse(id!!, name, task.map { it.toTaskResponse() })
    }

    fun toOwnerResponseLazy(): OwnerResponseLazy {
        return OwnerResponseLazy(id!!, name)
    }

    fun toOwnerResponseGraph(): OwnerResponseGraph {
        return OwnerResponseGraph(id!!, name, task.map { it.toTaskResponseGraph() })
    }

    fun toOwnerRequest(): OwnerRequest {
        return OwnerRequest(id!!, name)
    }
}

data class OwnerRequest(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("name")
    var name: String,
) : Serializable {
    fun toOwner(): Owner {
        return Owner(name)
    }
}

data class OwnerRequestToAdd(
    @JsonProperty("name")
    var name: String,
) : Serializable {
    fun toOwner(): Owner {
        return Owner(name)
    }
}

data class OwnerResponse(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("name")
    var name: String,
    val tasks: List<TaskResponse>
) : Serializable

data class OwnerResponseLazy(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("name")
    var name: String,
) : Serializable

data class OwnerResponseGraph(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("name")
    var name: String,
    val tasks: List<TaskResponseGraph>
) : Serializable