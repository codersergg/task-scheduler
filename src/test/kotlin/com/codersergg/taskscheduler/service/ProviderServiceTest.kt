package com.codersergg.taskscheduler.service

import com.codersergg.taskscheduler.dto.Duration
import com.codersergg.taskscheduler.dto.Timer
import com.codersergg.taskscheduler.dto.request.ProviderRequestToAdd
import com.codersergg.taskscheduler.dto.response.ProviderResponse
import com.codersergg.taskscheduler.dto.response.ProviderWithTaskResponse
import com.codersergg.taskscheduler.model.Provider
import com.codersergg.taskscheduler.model.Task
import com.codersergg.taskscheduler.repository.Pagination
import com.codersergg.taskscheduler.repository.ProviderRepository
import com.codersergg.taskscheduler.repository.RequestParameters
import com.codersergg.taskscheduler.repository.TaskRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.Instant

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@DirtiesContext
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProviderServiceTest
@Autowired constructor(
    val providerService: ProviderService,
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
    @DisplayName("getAllProvider()")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetAllProvider {

        @Test
        fun `should return List of Providers`() {
            // when
            val providers = providerService.getAllProviders(RequestParameters())

            // then
            Assertions.assertThat(providers.size).isEqualTo(3)
        }

        @Test
        fun `should return List of ProviderWithTask`() {
            // when
            val providers = providerService.getAllProvidersWithTask(RequestParameters())

            // then
            Assertions.assertThat(providers.size).isEqualTo(3)
        }

        @Test
        fun `should return List of Providers containing 2 elements from the first`() {
            // when
            val providers = providerService.getAllProviders(
                RequestParameters(
                    pagination = Pagination(0, 2)
                )
            )

            // then
            Assertions.assertThat(providers.size).isEqualTo(2)
            Assertions.assertThat(providers[0].id).isEqualTo(1)
            Assertions.assertThat(providers[0].name).isEqualTo("provider name 1")
            Assertions.assertThat(providers[1].id).isEqualTo(2)
            Assertions.assertThat(providers[1].name).isEqualTo("provider name 2")
        }

        @Test
        fun `should return List of Providers containing 2 elements from the second`() {
            // when
            val providers = providerService.getAllProviders(
                RequestParameters(
                    pagination = Pagination(0, 2)
                )
            )

            // then
            Assertions.assertThat(providers.size).isEqualTo(2)
            Assertions.assertThat(providers[0].id).isEqualTo(1)
            Assertions.assertThat(providers[0].name).isEqualTo("provider name 1")
            Assertions.assertThat(providers[1].id).isEqualTo(2)
            Assertions.assertThat(providers[1].name).isEqualTo("provider name 2")
        }

        @Test
        fun `should return List of ProvidersWithTask containing 1 elements from the first`() {
            // when
            val providers = providerService.getAllProvidersWithTask(
                RequestParameters(
                    pagination = Pagination(1, 2)
                )
            )

            // then
            Assertions.assertThat(providers.size).isEqualTo(2)
            Assertions.assertThat(providers[0].id).isEqualTo(2)
            Assertions.assertThat(providers[0].name).isEqualTo("provider name 2")
        }

        @Test
        fun `should return List of ProvidersWithTask containing 1 elements from the second`() {
            // when
            val providers = providerService.getAllProvidersWithTask(
                RequestParameters(
                    pagination = Pagination(1, 2)
                )
            )

            // then
            Assertions.assertThat(providers.size).isEqualTo(2)
            Assertions.assertThat(providers[0].id).isEqualTo(2)
            Assertions.assertThat(providers[0].name).isEqualTo("provider name 2")
        }
    }

    @Nested
    @DisplayName("getProvider(id: Long)")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetProviderById {

        @Test
        fun `should return Provider`() {
            // when
            val provider = providerService.getProvider(1)

            // then
            Assertions.assertThat(provider).isInstanceOf(ProviderResponse::class.java)
            Assertions.assertThat(provider.id).isEqualTo(1)
        }

        @Test
        fun `should return ProvidersWithTask`() {
            // when
            val provider = providerService.getProviderWithTasks(2)

            // then
            Assertions.assertThat(provider).isInstanceOf(ProviderWithTaskResponse::class.java)
            Assertions.assertThat(provider.id).isEqualTo(2)
        }
    }

    @Nested
    @DisplayName("createProvider()")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class CreateProvider {

        @Test
        fun `should create Provider`() {
            // when
            val name = "new Provider name"
            val provider = providerService.createProvider(ProviderRequestToAdd(name))

            // then
            Assertions.assertThat(provider).isInstanceOf(ProviderResponse::class.java)
            Assertions.assertThat(provider.id).isEqualTo(4)
            Assertions.assertThat(provider.name).isEqualTo(name)

            providerService.delete(provider.id)
        }
    }
}