package com.codersergg.taskscheduler.service

import com.codersergg.taskscheduler.repository.AppInitializerRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
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
internal class AppInitializerServiceTest @Autowired constructor(
    private val appInitializerRepository: AppInitializerRepository
) {
    companion object {
        @Container
        private val postgreSQLContainer = PostgreSQLContainer<Nothing>("postgres:latest")

        @DynamicPropertySource
        @JvmStatic
        fun registerDynamicProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgreSQLContainer::getUsername)
            registry.add("spring.datasource.password", postgreSQLContainer::getPassword)
        }

        @JvmStatic
        @AfterAll
        fun down() {
            postgreSQLContainer.stop()
        }
    }

    @Nested
    @DisplayName("Register")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class Register {

        @Test
        fun `should be registered, get current and get by uuid`() {
            // when
            val register = appInitializerRepository.register()

            // then
            assertThat(register).isTrue()
            val current = appInitializerRepository.getCurrent()
            assertThat(current).isNotNull
            val byUUID = appInitializerRepository.getByUUID(current.uuid)
            assertThat(current).isEqualTo(byUUID)
            assertThat(current.id).isEqualTo(byUUID.id)
            assertThat(current.uuid).isEqualTo(byUUID.uuid)
            assertThat(current.lastActivity).isEqualTo(byUUID.lastActivity)
            assertThat(current.lastUpdated).isEqualTo(byUUID.lastUpdated)
        }
    }
}