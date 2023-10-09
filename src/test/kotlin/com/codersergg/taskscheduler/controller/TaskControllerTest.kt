package com.codersergg.taskscheduler.controller

import com.codersergg.taskscheduler.dto.request.ProviderRequest
import com.codersergg.taskscheduler.dto.request.TaskToCreateRequest
import com.codersergg.taskscheduler.dto.request.TaskToUpdateRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*

@SpringBootTest
@AutoConfigureMockMvc
internal class TaskControllerTest
@Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {

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
                    jsonPath("$[0].owner.name") { value("task name 1") }
                    jsonPath("$[0].last_run") { isNotEmpty() }
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
                    jsonPath("$.owner.name") { value("task name 1") }
                    jsonPath("$.last_run") { isNotEmpty() }
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
            val owner3 = ProviderRequest(3, "task name 3")
            val task = TaskToCreateRequest(owner3)

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
                    jsonPath("$.owner.name") { value("task name 3") }
                    jsonPath("$.last_run") { isNotEmpty() }
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
            val owner = ProviderRequest(100, "task name will not be updated")
            val task = TaskToUpdateRequest(taskId, owner)

            // when
            val putRequest =
                mockMvc.put("/api/task") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(task)
                }

            // then
            val andReturnPut = putRequest
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                    }
                }
            assertThat(andReturnPut.andReturn().response.contentAsString).isEqualTo("1")

            mockMvc.get("/api/task/1") {
                contentType = MediaType.APPLICATION_JSON
            }.andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.id") { value(taskId) }
                    jsonPath("$.owner.name") { value("task name 1") }
                    jsonPath("$.last_run") { isNotEmpty() }
                }
        }
    }

    @Nested
    @DisplayName("DELETE /api/task/2")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteTask {

        @Test
        fun `should delete Task`() {

            val owner2 = ProviderRequest(2, "task name 2")
            val task = TaskToCreateRequest(owner2)

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