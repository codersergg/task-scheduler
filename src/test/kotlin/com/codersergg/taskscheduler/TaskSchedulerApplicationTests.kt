package com.codersergg.taskscheduler

import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
@DirtiesContext
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TaskSchedulerApplicationTests {

    companion object {
        @Container
        private val postgreSQLContainer = PostgreSQLContainer<Nothing>("postgres:latest")


        @DynamicPropertySource
        @JvmStatic
        fun registerDynamicProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgreSQLContainer::getUsername)
            registry.add("spring.datasource.password", postgreSQLContainer::getPassword)
            registry.add("spring.datasource.maxlifetime", postgreSQLContainer::getPassword)
        }

        @AfterAll
        @JvmStatic
        fun close() {
            postgreSQLContainer.stop()
        }
    }

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Test
    fun contextLoads() {
    }

    @Test
    @DirtiesContext
    fun `when database is connected then it should be Postgres version 16`() {
        val actualDatabaseVersion = jdbcTemplate.queryForObject("SELECT version()", String::class.java)
        actualDatabaseVersion shouldContain "PostgreSQL 16.0"
    }
}