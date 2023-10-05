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
    var tasks: MutableList<Task> = mutableListOf(),
    @Version
    var lastUpdated: LocalDateTime = LocalDateTime.now()
) : BaseEntity<Long>(), Serializable {
    fun toOwnerResponse(): OwnerResponse {
        return OwnerResponse(id!!, name)
    }

    fun toOwnerResponseWithTask(): OwnerResponseWithTask {
        return OwnerResponseWithTask(id!!, name, tasks.map { it.toTaskResponseGraph() })
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
) : Serializable

data class OwnerResponseWithTask(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("name")
    var name: String,
    val tasks: List<TaskResponseWithTask>
) : Serializable