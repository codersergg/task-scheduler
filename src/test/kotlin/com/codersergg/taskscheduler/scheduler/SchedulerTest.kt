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
import java.net.URI
import java.time.Instant

internal class SchedulerTest {

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
                        Scheduler.run(
                            function1, DurationRestTask(
                                DefaultProvider("Provider name"),
                                Instant.now(),
                                Duration(timeMillis),
                                pathResponse = RestPathResponse(URI("http://localhost:8080/api/test")),
                            )
                        )
                        Scheduler.run(
                            function2, DurationRestTask(
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