package com.codersergg.taskscheduler.service

import com.codersergg.taskscheduler.dto.Duration
import com.codersergg.taskscheduler.dto.request.ProviderRequest
import com.codersergg.taskscheduler.dto.request.TaskToCreateRequest
import com.codersergg.taskscheduler.dto.response.TaskResponseWithTask
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
            val taskResponse: TaskResponseWithTask = taskService.getTask(1)

            // then
            Assertions.assertThat(taskResponse.id).isEqualTo(1)
            Assertions.assertThat(taskResponse.createdAt).isNotNull
        }
    }

    @Nested
    @DisplayName("TaskRepository findAll()")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class FindAll {

        @Test
        fun `should return List of TaskResponse`() {
            // when
            val findAll: List<TaskResponseWithTask> = taskService.getAllTasks()

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
            val provider = ProviderRequest(3, "new task name")
            val task = TaskToCreateRequest(provider, delay = Duration(5000))

            // when
            val taskResponse: TaskResponseWithTask = taskService.createTask(task)

            // then
            Assertions.assertThat(taskResponse).isNotNull
            Assertions.assertThat(taskResponse.createdAt).isNotNull
        }
    }
}