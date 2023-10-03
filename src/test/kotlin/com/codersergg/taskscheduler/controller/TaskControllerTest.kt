package com.codersergg.taskscheduler.controller

import com.codersergg.taskscheduler.model.TaskRequestToCreate
import com.codersergg.taskscheduler.model.TaskRequestToUpdate
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
                    jsonPath("$[0].task_name") { value("task name 1") }
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
                    jsonPath("$.task_name") { value("task name 1") }
                    jsonPath("$.last_run") { isNotEmpty() }
                }
        }

        @Test
        fun `should return Not Found`() {
            mockMvc.get("/api/task/5") {
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
            val task = TaskRequestToCreate("new task name")

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
                    jsonPath("$.id") { value(4) }
                    jsonPath("$.task_name") { value("new task name") }
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
            val task = TaskRequestToUpdate(taskId, "task name will not be updated")

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
                    jsonPath("$.task_name") { value("task name 1") }
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
            mockMvc.delete("/api/task/2") {
                contentType = MediaType.APPLICATION_JSON
            }.andDo { print() }
                .andExpect {
                    status { isNoContent() }
                }
        }
    }
}