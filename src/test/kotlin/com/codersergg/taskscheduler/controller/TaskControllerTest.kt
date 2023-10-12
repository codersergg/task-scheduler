package com.codersergg.taskscheduler.controller

import com.codersergg.taskscheduler.dto.Duration
import com.codersergg.taskscheduler.dto.Timer
import com.codersergg.taskscheduler.dto.request.ProviderRequest
import com.codersergg.taskscheduler.dto.request.TaskToCreateRequest
import com.codersergg.taskscheduler.dto.request.TaskToUpdateRequest
import com.codersergg.taskscheduler.model.Provider
import com.codersergg.taskscheduler.model.Task
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
            val owner3 = ProviderRequest(3, "provider name 3")
            val task = TaskToCreateRequest(owner3, delay = Duration(5000))

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
                    jsonPath("$.id") { value(6) }
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
        fun `should update Task`() {
            // given
            val taskId: Long = 1
            val provider = ProviderRequest(100, "task name will not be updated")
            val task = TaskToUpdateRequest(taskId, provider, delay = Duration(5000))

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

            val provider2 = Provider("provider name 2")
            val task = Task(provider2, Instant.now(), Instant.EPOCH, Duration(5))

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