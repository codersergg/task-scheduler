package com.codersergg.taskscheduler.controller

import com.codersergg.taskscheduler.dto.*
import com.codersergg.taskscheduler.model.Provider
import com.codersergg.taskscheduler.model.Task
import com.codersergg.taskscheduler.model.json.Duration
import com.codersergg.taskscheduler.model.json.RestPathResponse
import com.codersergg.taskscheduler.repository.ProviderRepository
import com.codersergg.taskscheduler.repository.TaskRepository
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
import org.springframework.test.web.servlet.*
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.net.URI
import java.time.Instant

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@DirtiesContext
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal class TaskControllerTest
@Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper,
    providerRepository: ProviderRepository,
    taskRepository: TaskRepository
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
            val provider1 =
                providerRepository.save(Provider("provider name 1", type = ProviderType.DEFAULT_PROVIDER.string))
            val provider2 =
                providerRepository.save(Provider("provider name 2", type = ProviderType.DEFAULT_PROVIDER.string))
            val provider3 =
                providerRepository.save(Provider("provider name 3", type = ProviderType.DEFAULT_PROVIDER.string))

            taskRepository.save(
                Task(
                    provider = provider1,
                    delay = Duration(5),
                    pathResponse = RestPathResponse(URI("http://localhost:8080/api/test"))
                )
            )
            taskRepository.save(
                Task(
                    provider = provider1,
                    delay = Duration(5),
                    pathResponse = RestPathResponse(URI("http://localhost:8080/api/test"))
                )
            )
            taskRepository.save(
                Task(
                    provider = provider1,
                    delay = Duration(5),
                    pathResponse = RestPathResponse(URI("http://localhost:8080/api/test"))
                )
            )
            taskRepository.save(
                Task(
                    provider = provider2,
                    delay = Duration(5),
                    pathResponse = RestPathResponse(URI("http://localhost:8080/api/test"))
                )
            )
            taskRepository.save(
                Task(
                    provider = provider3,
                    delay = Duration(5),
                    pathResponse = RestPathResponse(URI("http://localhost:8080/api/test"))
                )
            )
        }
    }

    @Nested
    @DisplayName("GET /api/task")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetAllTasks {

        @Test
        fun `should return List of Tasks`() {
            mockMvc.get("/api/task") {
                contentType = MediaType.APPLICATION_JSON
            }.andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$[0].id") { value("1") }
                    jsonPath("$[0].provider.name") { value("provider name 1") }
                    jsonPath("$[0].lastRun") { isNotEmpty() }
                }
        }
    }

    @Nested
    @DisplayName("GET /api/task/1")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetTask {

        @Test
        fun `should return Task`() {
            mockMvc.get("/api/task/1") {
                contentType = MediaType.APPLICATION_JSON
            }.andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.id") { value(1L) }
                    jsonPath("$.provider.name") { value("provider name 1") }
                    jsonPath("$.lastRun") { isNotEmpty() }
                }
        }

        @Test
        fun `should return Not Found`() {
            mockMvc.get("/api/task/100") {
                contentType = MediaType.APPLICATION_JSON
            }.andDo { print() }
                .andExpect {
                    status { isNotFound() }
                    content { contentType("text/plain;charset=UTF-8") }

                }
        }
    }

    @Nested
    @DisplayName("POST /api/task")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PostTask {
        @Test
        fun `should add Task`() {
            // given
            val name = "provider name 3"
            val defaultProvider3 = DefaultProvider(name = name)
            val task = DurationRestTask(
                defaultProvider3,
                createdAt = Instant.now(),
                delay = Duration(5000),
                pathResponse = RestPathResponse(URI("http://localhost:8080/api/test"))
            )

            // when
            val postRequest = mockMvc.post("/api/task") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(task)
            }

            // then
            postRequest
                .andDo { print() }
                .andExpect {
                    status { isCreated() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                    }
                    jsonPath("$.id") { value(7) }
                    jsonPath("$.provider.name") { value("provider name 3") }
                    jsonPath("$.lastRun") { isNotEmpty() }
                }
        }
    }

    @Nested
    @DisplayName("PUT /api/task")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PutTask {
        @Test
        fun `should not be updated`() {
            // given
            val taskId: Long = 1
            val defaultProvider = DefaultProvider("provider name 3")
            val task = TaskToUpdateRequest(
                taskId,
                defaultProvider,
                createdAt = Instant.now(),
                delay = Duration(5000),
                pathResponse = RestPathResponse(URI("http://localhost:8080/api/test"))
            )

            // when
            val putRequest =
                mockMvc.put("/api/task") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(task)
                }

            // then
            putRequest
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                }

            mockMvc.get("/api/task/1") {
                contentType = MediaType.APPLICATION_JSON
            }.andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.id") { value(taskId) }
                    jsonPath("$.provider.name") { value("provider name 1") }
                    jsonPath("$.lastRun") { isNotEmpty() }
                }
        }
    }

    @Nested
    @DisplayName("DELETE /api/task/2")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteTask {

        @Test
        fun `should delete Task`() {

            val provider2 = Provider("provider name 2", type = ProviderType.DEFAULT_PROVIDER.string)
            val task = Task(
                provider2,
                delay = Duration(5),
                pathResponse = RestPathResponse(URI("http://localhost:8080/api/test"))
            )

            // when
            mockMvc.post("/api/task") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(task)
            }

            mockMvc.delete("/api/task/6") {
                contentType = MediaType.APPLICATION_JSON
            }.andDo { print() }
                .andExpect {
                    status { isNoContent() }
                }
        }
    }
}