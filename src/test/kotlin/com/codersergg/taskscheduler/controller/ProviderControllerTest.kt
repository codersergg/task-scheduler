package com.codersergg.taskscheduler.controller

import com.codersergg.taskscheduler.dto.Duration
import com.codersergg.taskscheduler.dto.Timer
import com.codersergg.taskscheduler.model.Provider
import com.codersergg.taskscheduler.model.Task
import com.codersergg.taskscheduler.repository.Pagination
import com.codersergg.taskscheduler.repository.ProviderRepository
import com.codersergg.taskscheduler.repository.RequestParameters
import com.codersergg.taskscheduler.repository.TaskRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.Instant


@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
internal class ProviderControllerTest
@Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper,
    val providerRepository: ProviderRepository,
    val taskRepository: TaskRepository
) {

    @Container
    var postgresContainer = PostgreSQLContainer("postgres:13.3")
        .withUsername("myuser")
        .withPassword("mypassword")
        .withDatabaseName("mydatabase")
        .withReuse(false)

    @Nested
    @DisplayName("GET /api/provider")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetAllOwners {

        private val baseUrl = "/api/provider"

        @BeforeAll
        fun setUp() {
            setPropertyContainer()
        }

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

    private fun setPropertyContainer() {
        postgresContainer.host
        postgresContainer.databaseName
        postgresContainer.username
        postgresContainer.password
        postgresContainer.start()
        init()
    }

    private fun init() {
        val provider1 = providerRepository.save(Provider("provider name 1"))
        val provider2 = providerRepository.save(Provider("provider name 2"))
        val provider3 = providerRepository.save(Provider("provider name 3"))

        val createdAt = Instant.now()
        taskRepository.saveAndFlush(Task(provider1, createdAt, Instant.EPOCH, Duration(5)))
        taskRepository.save(Task(provider1, Instant.now(), Instant.EPOCH, Duration(5))).toTaskResponseWithDelay()
        taskRepository.save(Task(provider1, Instant.now(), Instant.EPOCH, Duration(5)))
        taskRepository.save(Task(provider2, Instant.now(), Instant.EPOCH, Duration(5)))
        taskRepository.save(Task(provider3, Instant.now(), Instant.EPOCH, Timer(1, 2, 11, 30)))
    }
}

