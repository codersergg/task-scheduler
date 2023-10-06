package com.codersergg.taskscheduler.repository

import jakarta.persistence.EntityManagerFactory
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.springframework.stereotype.Component

@Component
class EntityManagerFactoryService(private val factory: EntityManagerFactory) {

    fun session(): Session {
        factory as SessionFactory
        return factory.openSession()
    }
}