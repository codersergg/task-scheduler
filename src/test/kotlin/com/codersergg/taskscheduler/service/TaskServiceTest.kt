package com.codersergg.taskscheduler.service

import com.codersergg.taskscheduler.dto.request.ProviderRequest
import com.codersergg.taskscheduler.dto.request.TaskToCreateRequest
import com.codersergg.taskscheduler.dto.response.TaskResponse
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class TaskServiceTest(@Autowired val taskService: TaskService) {

    @Nested
    @DisplayName("TaskRepository findById()")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class FindById {

        @Test
        fun `should return TaskResponse`() {
            // when
            val findById: TaskResponse = taskService.getTask(1)

            // then
            Assertions.assertThat(findById.id).isEqualTo(1)
            Assertions.assertThat(findById.owner.name).isEqualTo("task name 1")
            org.junit.jupiter.api.Assertions.assertNotNull(findById.lastRun)
        }
    }

    @Nested
    @DisplayName("TaskRepository findAll()")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class FindAll {

        @Test
        fun `should return List of TaskResponse`() {
            // when
            val findAll: List<TaskResponse> = taskService.getAllTasks()

            // then
            Assertions.assertThat(findAll).isNotEmpty
            Assertions.assertThat(findAll.size).isGreaterThanOrEqualTo(2)

        }
    }

    @Nested
    @DisplayName("TaskRepository createTask()")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class CreateTask {

        @Test
        fun `should return TaskResponse`() {
            // given
            val owner = ProviderRequest(3, "new task name")
            val task = TaskToCreateRequest(owner)

            // when
            val taskResponse: TaskResponse = taskService.createTask(task)

            // then
            Assertions.assertThat(taskResponse).isNotNull
            Assertions.assertThat(taskResponse.owner.name).isEqualTo("task name 3")

        }
    }
}