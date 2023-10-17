package com.codersergg.taskscheduler.controller

import com.codersergg.taskscheduler.dto.DefaultProvider
import com.codersergg.taskscheduler.dto.DurationRestTask
import com.codersergg.taskscheduler.dto.TaskType
import com.codersergg.taskscheduler.model.Provider
import com.codersergg.taskscheduler.model.Task
import com.codersergg.taskscheduler.model.json.Duration
import com.codersergg.taskscheduler.model.json.RestPathResponse
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.matchers.string.contain
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
internal class SchedulerTestControllerTest
@Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper
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
    @DisplayName("POST api/test")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PostTask {

        private val baseUrl = "http://localhost:8080/api/test"

        @Test
        fun `should receive POST DurationRestTask`() {
            // when
            val task = DurationRestTask(
                1,
                provider = DefaultProvider("name"),
                delay = Duration(5000),
                pathResponse = RestPathResponse(URI(baseUrl))
            )
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
        }

        @Test
        fun `should return BAD_REQUEST`() {
            // when
            val task = Task(
                provider = Provider("provider name 1", type = "type"),
                delay = Duration(5),
                pathResponse = RestPathResponse(URI(baseUrl)),
                type = TaskType.DURATION_REST_TASK.string
            )
            val postRequest = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(task)
            }
            // then
            postRequest
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                    content {
                        contentType("text/plain;charset=UTF-8")
                    }
                    jsonPath("$") { contain("JSON parse error: Could not resolve type") }
                }
        }
    }
}