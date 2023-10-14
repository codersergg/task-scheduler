package com.codersergg.taskscheduler.model

import jakarta.persistence.*
import org.springframework.data.util.ProxyUtils
import java.io.Serializable
import java.time.Instant

@MappedSuperclass
abstract class BaseEntity<T : Serializable> {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: T? = null

    @Version
    var lastUpdated: Instant = Instant.now()

    override fun equals(other: Any?): Boolean {
        other ?: return false

        if (this === other) return true

        if (javaClass != ProxyUtils.getUserClass(other)) return false

        other as BaseEntity<*>

        return this.id != null && this.id == other.id
    }

    override fun hashCode() = 25

    override fun toString(): String {
        return "${this.javaClass.simpleName}(id=$id)"
    }
}