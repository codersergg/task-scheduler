package com.codersergg.taskscheduler.scheduler

import com.codersergg.taskscheduler.dto.AbstractTask
import com.codersergg.taskscheduler.dto.DurationRestTask
import com.codersergg.taskscheduler.dto.RestTask
import com.codersergg.taskscheduler.dto.TimerRestTask
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*
import org.joda.time.DateTimeZone
import java.time.Duration
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

object Scheduler {

    private val logger = KotlinLogging.logger {}
    fun run(block: suspend () -> Unit, task: RestTask) {
        logger.info { "Start run task: $task" }
        when (task) {
            is DurationRestTask -> runWithDuration(task, block)
            is TimerRestTask -> runWithTimer(task, block)
            else -> {
                throw ClassCastException()
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun runWithTimer(task: TimerRestTask, block: suspend () -> Unit) {
        GlobalScope.launch {
            val zoneId = DateTimeZone.forOffsetHours(task.delay.zoneId).toTimeZone().toZoneId()
            val delay = Duration.of(task.delay.hours, ChronoUnit.HOURS).plusMinutes(task.delay.minutes)
            while (isActive) {
                val now = ZonedDateTime.now(zoneId)
                val midnight = now.truncatedTo(ChronoUnit.DAYS)
                    .withHour(0)
                    .withMinute(0)
                    .withSecond(0)
                    .withNano(0)
                val difference = Duration.between(now, midnight.plus(delay))
                val duration = if (difference.isNegative) {
                    logger.info { "delay: ${Duration.ofDays(task.delay.delayInDays).plus(difference)} for $task" }
                    Duration.ofDays(task.delay.delayInDays).plus(difference).toMillis()
                } else {
                    logger.info { "delay: $difference for $task" }
                    difference.toMillis()
                }
                delay(duration)
                runTask(task, block)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun runWithDuration(task: DurationRestTask, block: suspend () -> Unit) {
        GlobalScope.launch {
            while (isActive) {
                delay(task.delay.value)
                runTask(task, block)
            }
        }
    }

    private suspend fun runTask(task: AbstractTask, block: suspend () -> Unit) {
        logger.info { "Start new run task cycle: $task" }
        block()
        logger.info { "End success new run task cycle: $task" }
    }
}