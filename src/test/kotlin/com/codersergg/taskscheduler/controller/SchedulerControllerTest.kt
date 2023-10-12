package com.codersergg.taskscheduler.controller

import com.codersergg.taskscheduler.dto.*
import com.codersergg.taskscheduler.model.Provider
import com.codersergg.taskscheduler.repository.ProviderRepository
import com.codersergg.taskscheduler.repository.TaskRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.net.URI

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@DirtiesContext
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal class SchedulerControllerTest
@Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    providerRepository: ProviderRepository,
    private val taskRepository: TaskRepository
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

    init {
        if (providerRepository.findAll().isEmpty()) {
            providerRepository.save(Provider("provider name Init"))
        }
    }

    @Nested
    @DisplayName("POST api/scheduler")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetAllOwners {
        private val baseUrl = "/api/scheduler"

        @Test
        fun `should save TimerRestTask and run Task`() {
            // given
            val name = "provider new name"
            val owner3 = DefaultProvider(name)
            val task = TimerRestTask(
                owner3,
                delay = Timer(
                    value = 1,
                    zoneId = 2,
                    hours = 22,
                    minutes = 24
                ),
                pathResponse = RestPathResponse(URI("http://localhost:8080/api/test-scheduler-timer"))
            )

            // when
            val postRequest = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(task)
            }

            // then
            postRequest
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                    }
                    jsonPath("$") { value(true) }
                }
            val findById = taskRepository.findById(1)
            assertThat(findById.isPresent).isTrue()
            assertThat(findById.get().provider.name).isEqualTo(name)
            assertThat(findById.get().delay.value).isEqualTo(1L)
            assertThat(findById.get().pathResponse).isEqualTo(RestPathResponse(URI("http://localhost:8080/api/test-scheduler-timer")))
            taskRepository.deleteById(findById.get().id!!)
        }

        @Test
        fun `should save DurationRestTask and run Task`() {
            // given
            val name = "provider name Init"
            val provider = DefaultProvider(name)
            val task = DurationRestTask(
                provider,
                delay = Duration(15000),
                pathResponse = RestPathResponse(URI("http://localhost:8080/api/test-scheduler"))
            )

            // when
            val postRequest = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(task)
            }

            // then
            postRequest
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                    }
                    jsonPath("$") { value(true) }
                }
            val findById = taskRepository.findById(2)
            assertThat(findById.isPresent).isTrue()
            assertThat(findById.get().provider.name).isEqualTo(name)
            assertThat(findById.get().delay.value).isEqualTo(15000L)
            assertThat(findById.get().pathResponse).isEqualTo(RestPathResponse(URI("http://localhost:8080/api/test-scheduler")))
            taskRepository.deleteById(findById.get().id!!)
        }
    }
}