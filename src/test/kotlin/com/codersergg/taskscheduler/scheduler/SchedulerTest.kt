package com.codersergg.taskscheduler.scheduler

import kotlinx.coroutines.*
import kotlinx.coroutines.time.withTimeout
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.Duration

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
            val timeMillis: Long = 100
            val duration = kotlin.time.Duration.parse("0.1s")
            val function = {
                result++
                println("Simple text")
            }

            // when
            //assertTimeoutPreemptively(Duration.ofMillis(20 * timeMillis)) {
            GlobalScope.launch {
                withTimeout(Duration.ofMillis(5 * timeMillis)) {
                    Scheduler.run(function, duration)
                }
                while (isActive) {
                    delay(duration)
                    println("Count: ${++count}")
                }
            }
            //}
            // then
            runBlocking {
                delay(6 * timeMillis)
                assertEquals(count, result)
            }
        }
    }
}