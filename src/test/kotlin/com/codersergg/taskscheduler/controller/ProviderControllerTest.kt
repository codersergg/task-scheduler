package com.codersergg.taskscheduler.controller

import com.codersergg.taskscheduler.dto.ProviderType
import com.codersergg.taskscheduler.dto.TaskType
import com.codersergg.taskscheduler.model.Provider
import com.codersergg.taskscheduler.model.Task
import com.codersergg.taskscheduler.model.json.Duration
import com.codersergg.taskscheduler.model.json.RestPathResponse
import com.codersergg.taskscheduler.repository.Pagination
import com.codersergg.taskscheduler.repository.ProviderRepositoryJpa
import com.codersergg.taskscheduler.repository.RequestParameters
import com.codersergg.taskscheduler.repository.TaskRepositoryJpa
import com.fasterxml.jackson.databind.ObjectMapper
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
import org.springframework.test.web.servlet.get
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.net.URI


@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@DirtiesContext
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal class ProviderControllerTest
@Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper,
    providerRepositoryJpa: ProviderRepositoryJpa,
    taskRepositoryJpa: TaskRepositoryJpa
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
        if (providerRepositoryJpa.findAll().isEmpty()) {
            val provider1 =
                providerRepositoryJpa.save(Provider("provider name 1", type = ProviderType.DEFAULT_PROVIDER.string))
            val provider2 =
                providerRepositoryJpa.save(Provider("provider name 2", type = ProviderType.DEFAULT_PROVIDER.string))
            val provider3 =
                providerRepositoryJpa.save(Provider("provider name 3", type = ProviderType.DEFAULT_PROVIDER.string))

            taskRepositoryJpa.save(
                Task(
                    provider = provider1,
                    delay = Duration(5),
                    pathResponse = RestPathResponse(URI("http://localhost:8080/api/test")),
                    type = TaskType.DURATION_REST_TASK.string
                )
            )
            taskRepositoryJpa.save(
                Task(
                    provider = provider1,
                    delay = Duration(5),
                    pathResponse = RestPathResponse(URI("http://localhost:8080/api/test")),
                    type = TaskType.DURATION_REST_TASK.string
                )
            )
            taskRepositoryJpa.save(
                Task(
                    provider = provider1,
                    delay = Duration(5),
                    pathResponse = RestPathResponse(URI("http://localhost:8080/api/test")),
                    type = TaskType.DURATION_REST_TASK.string
                )
            )
            taskRepositoryJpa.save(
                Task(
                    provider = provider2,
                    delay = Duration(5),
                    pathResponse = RestPathResponse(URI("http://localhost:8080/api/test")),
                    type = TaskType.DURATION_REST_TASK.string
                )
            )
            taskRepositoryJpa.save(
                Task(
                    provider = provider3,
                    delay = Duration(5),
                    pathResponse = RestPathResponse(URI("http://localhost:8080/api/test")),
                    type = TaskType.DURATION_REST_TASK.string
                )
            )
        }
    }

    @Nested
    @DisplayName("GET /api/provider")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetAllOwners {
        private val baseUrl = "/api/provider"

        @Test
        fun `should return List of Providers`() {
            mockMvc.get(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
            }.andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$[0].id") { value("1") }
                    jsonPath("$[0].name") { value("provider name 1") }
                }
        }

        @Test
        fun `should return List of Owners body null`() {
            mockMvc.get(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(null)
            }.andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$[0].id") { value("1") }
                    jsonPath("$[0].name") { value("provider name 1") }
                }
        }

        @Test
        fun `should return List of Owners containing 2 elements from the second`() {
            val parameters = RequestParameters(
                pagination = Pagination(2, 2)
            )
            println(objectMapper.writeValueAsString(parameters))

            mockMvc.get(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(parameters)
            }.andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$[0].id") { value("1") }
                    jsonPath("$[0].name") { value("provider name 1") }
                }
        }
    }

    @Nested
    @DisplayName("GET /api/provider/task")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetAllOwnersWithTasks {
        private val baseUrl = "/api/provider/task"

        @Test
        fun `should return List of Owners`() {
            mockMvc.get(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
            }.andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$[0].id") { value("1") }
                    jsonPath("$[0].name") { value("provider name 1") }
                    jsonPath("$[0].tasks[0].lastRun") { isNotEmpty() }
                }
        }

        @Test
        fun `should return List of Owners body null`() {
            mockMvc.get(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(null)
            }.andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$[0].id") { value("1") }
                    jsonPath("$[0].name") { value("provider name 1") }
                    jsonPath("$[0].tasks[0].lastRun") { isNotEmpty() }
                }
        }

        @Test
        fun `should return List of Owners containing 2 elements from the second`() {
            val parameters = RequestParameters(
                pagination = Pagination(2, 2)
            )
            mockMvc.get(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(parameters)
            }.andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$[0].id") { value("1") }
                    jsonPath("$[0].name") { value("provider name 1") }
                    jsonPath("$[0].tasks[0].lastRun") { isNotEmpty() }
                }
        }
    }
}