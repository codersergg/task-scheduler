package com.codersergg.taskscheduler.scheduler

import com.codersergg.taskscheduler.dto.DefaultProvider
import com.codersergg.taskscheduler.dto.DurationRestTask
import com.codersergg.taskscheduler.model.json.Duration
import com.codersergg.taskscheduler.model.json.RestPathResponse
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.time.withTimeout
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
import java.time.Instant

@SpringBootTest
@Testcontainers
@DirtiesContext
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal class SchedulerTest(@Autowired private val scheduler: Scheduler) {
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
    @DisplayName("Scheduler run simple test")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class RunSimpleTask {

        @OptIn(DelicateCoroutinesApi::class)
        @Test
        fun `should print simple text`() = runTest {
            // given
            var count = 0
            var result = 0
            val timeMillis: Long = 250
            val duration = kotlin.time.Duration.parse("0.25s")
            val function1 = {
                result++
                println("Simple text 1")
            }
            val function2 = {
                result++
                println("Simple text 2")
            }

            // when
            assertTimeoutPreemptively(java.time.Duration.ofMillis(20 * timeMillis)) {
                GlobalScope.launch {
                    withTimeout(java.time.Duration.ofMillis(5 * timeMillis)) {
                        scheduler.run(
                            function1, DurationRestTask(
                                1,
                                DefaultProvider("Provider name"),
                                Instant.now(),
                                Duration(timeMillis),
                                pathResponse = RestPathResponse(URI("http://localhost:8080/api/test")),
                            )
                        )
                        scheduler.run(
                            function2, DurationRestTask(
                                2,
                                DefaultProvider("Other provider name"),
                                Instant.now(),
                                Duration(timeMillis),
                                pathResponse = RestPathResponse(URI("http://localhost:8080/api/test")),
                            )
                        )
                    }
                    while (isActive) {
                        delay(duration)
                        ++count
                        ++count
                    }
                    // then
                    assertTimeoutPreemptively(java.time.Duration.ofMillis(8 * timeMillis)) {
                        runTest {
                            delay(7 * timeMillis)
                            Assertions.assertThat(count).isLessThanOrEqualTo(result)
                        }
                    }
                }
            }
        }
    }
}