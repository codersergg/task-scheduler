package com.codersergg.taskscheduler.repository

import com.codersergg.taskscheduler.dto.ProviderType
import com.codersergg.taskscheduler.dto.TaskType
import com.codersergg.taskscheduler.model.Provider
import com.codersergg.taskscheduler.model.Task
import com.codersergg.taskscheduler.model.json.Duration
import com.codersergg.taskscheduler.model.json.RestPathResponse
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.net.URI

@SpringBootTest
@Testcontainers
@DirtiesContext
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal class TaskRepositoryTest
@Autowired constructor(
    val taskRepository: TaskRepository,
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
    @DisplayName("TaskRepository findById()")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class FindById {

        @Test
        fun `should return TaskResponse`() {
            // when
            val taskResponse: Task = taskRepository.getTask(1)

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
            val findAll: List<Task> = taskRepository.getAllTasks()

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
        fun `should create new Provider and Task, return TaskResponse`() = runTest {
            // given
            val name = "New Provider"
            val provider = Provider(name, type = ProviderType.DEFAULT_PROVIDER.string)
            val task = Task(
                provider,
                delay = Duration(5000),
                pathResponse = RestPathResponse(URI("http://localhost:8080/api/test")),
                type = TaskType.DURATION_REST_TASK.string
            )

            // when
            val taskResponse = taskRepository.createTask(task)

            // then
            Assertions.assertThat(taskResponse).isNotNull
            Assertions.assertThat(taskResponse.createdAt).isNotNull
            Assertions.assertThat(taskResponse.provider.name).isEqualTo(name)
            Assertions.assertThat(taskResponse.delay.value).isEqualTo(5000L)
            //Assertions.assertThat(taskResponse.lastRun).isEqualTo(Instant.EPOCH)
            //Assertions.assertThat(Instant.now()).isAfter(taskResponse.createdAt)
        }

        @Test
        fun `should create new Task and use old Provider, return TaskResponse`() = runTest {
            // given
            val name = "provider name 3"
            val provider = Provider(name, type = ProviderType.DEFAULT_PROVIDER.string)
            val task = Task(
                provider,
                delay = Duration(5000),
                pathResponse = RestPathResponse(URI("http://localhost:8080/api/test")),
                type = TaskType.DURATION_REST_TASK.string
            )

            // when
            val taskResponse = taskRepository.createTask(task)

            // then
            Assertions.assertThat(taskResponse).isNotNull
            Assertions.assertThat(taskResponse.createdAt).isNotNull
            Assertions.assertThat(taskResponse.provider.name).isEqualTo(name)
            Assertions.assertThat(taskResponse.delay.value).isEqualTo(5000L)
            //Assertions.assertThat(taskResponse.lastRun).isEqualTo(Instant.EPOCH)
            //Assertions.assertThat(Instant.now()).isAfter(taskResponse.createdAt)
        }
    }
}