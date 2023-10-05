package com.codersergg.taskscheduler.configuration

import com.codersergg.taskscheduler.model.Owner
import com.codersergg.taskscheduler.model.Task
import com.codersergg.taskscheduler.repository.OwnerRepository
import com.codersergg.taskscheduler.repository.TaskRepository
import jakarta.persistence.Persistence
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.cfg.JdbcSettings.*
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import java.lang.Boolean.TRUE
import java.time.Instant


@org.springframework.context.annotation.Configuration
class Configuration {
    @Bean
    fun databaseInitializer(
        ownerRepository: OwnerRepository,
        taskRepository: TaskRepository
    ) = ApplicationRunner {
        /*val owner1 = ownerRepository.save(Owner("task name 1"))
        val owner2 = ownerRepository.save(Owner("task name 2"))
        val owner3 = ownerRepository.save(Owner("task name 3"))

        taskRepository.save(Task(owner1, Instant.now()))
        taskRepository.save(Task(owner1, Instant.now()))
        taskRepository.save(Task(owner1, Instant.now()))
        taskRepository.save(Task(owner2, Instant.now()))
        taskRepository.save(Task(owner3, Instant.now()))*/
    }


    /*fun sessionFactory(): SessionFactory {
        org.hibernate.cfg.Configuration()
            .addAnnotatedClass(
                Owner.class)
                    // use H2 in-memory database
                    .setProperty(URL, "jdbc:h2:mem:db1")
                    .setProperty(USER, "sa")
                    .setProperty(PASS, "")
                    // use Agroal connection pool
                    .setProperty("hibernate.agroal.maxSize", "20")
                    // display SQL in console
                    .setProperty(SHOW_SQL, TRUE.toString())
                    .setProperty(FORMAT_SQL, TRUE.toString())
                    .setProperty(HIGHLIGHT_SQL, TRUE.toString())
                    .buildSessionFactory()
    }*/

    /*@Bean
    fun getCurrentSessionFromJPA(): SessionFactory? {
        // JPA and Hibernate SessionFactory example
        val emf = Persistence.createEntityManagerFactory("codersergg")
        val entityManager = emf.createEntityManager()
        // Get the Hibernate Session from the EntityManager in JPA
        val session: Session =
            entityManager.unwrap(Session::class.java)
        return session.sessionFactory
    }*/
}