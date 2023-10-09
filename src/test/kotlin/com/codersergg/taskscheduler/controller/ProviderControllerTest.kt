package com.codersergg.taskscheduler.controller

import com.codersergg.taskscheduler.repository.Pagination
import com.codersergg.taskscheduler.repository.RequestParameters
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
internal class ProviderControllerTest
@Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {
    @Nested
    @DisplayName("GET /api/provider")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetAllOwners {
        private val baseUrl = "/api/provider"
        @Test
        fun `should return List of Owners`() {
            mockMvc.get(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
            }.andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$[0].id") { value("1") }
                    jsonPath("$[0].name") { value("task name 1") }
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
                    jsonPath("$[0].name") { value("task name 1") }
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
                    jsonPath("$[0].name") { value("task name 1") }
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
                    jsonPath("$[0].name") { value("task name 1") }
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
                    jsonPath("$[0].name") { value("task name 1") }
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
                    jsonPath("$[0].name") { value("task name 1") }
                    jsonPath("$[0].tasks[0].lastRun") { isNotEmpty() }
                }
        }
    }
}