package com.codersergg.taskscheduler.scheduler

import com.codersergg.taskscheduler.dto.DefaultProvider
import com.codersergg.taskscheduler.dto.DurationRestTask
import com.codersergg.taskscheduler.dto.Duration
import kotlinx.coroutines.*
import kotlinx.coroutines.time.withTimeout
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.*
import java.net.URI
import java.time.Instant

class SchedulerTest {

    @Nested
    @DisplayName("Scheduler run simple test")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class RunSimpleTask {

        @Test
        @DelicateCoroutinesApi
        fun `should print simple text`() {
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
                                URI("http://localhost:8080/api/test"),
                                Duration(timeMillis)
                            )
                        )
                        Scheduler.run(
                            function2, DurationRestTask(
                                DefaultProvider("Other provider name"),
                                Instant.now(),
                                URI("http://localhost:8080/api/test"),
                                Duration(timeMillis)
                            )
                        )
                    }
                    while (isActive) {
                        delay(duration)
                        ++count
                        ++count
                    }
                }
            }
            // then
            runBlocking {
                delay(7 * timeMillis)
                Assertions.assertThat(count).isLessThanOrEqualTo(result)
            }
        }
    }
}