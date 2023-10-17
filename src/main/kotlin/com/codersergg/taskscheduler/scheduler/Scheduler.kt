package com.codersergg.taskscheduler.scheduler

import com.codersergg.taskscheduler.dto.AbstractTask
import com.codersergg.taskscheduler.dto.DurationRestTask
import com.codersergg.taskscheduler.dto.RestTask
import com.codersergg.taskscheduler.dto.TimerRestTask
import com.codersergg.taskscheduler.repository.TaskRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*
import org.joda.time.DateTimeZone
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

@Component
class Scheduler(private val taskRepository: TaskRepository) {

    private val logger = KotlinLogging.logger {}

    suspend fun run(block: suspend () -> Unit, task: AbstractTask) {
        logger.info { "Start run task: $task" }
        when (task) {
            is RestTask -> {
                when (task) {
                    is DurationRestTask -> runWithDuration(task, block)
                    is TimerRestTask -> runWithTimer(task, block)
                    else -> {
                        throw ClassCastException()
                    }
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun runWithTimer(task: TimerRestTask, block: suspend () -> Unit) {
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
                    logger.info { "delay: ${Duration.ofDays(task.delay.value).plus(difference)} for $task" }
                    Duration.ofDays(task.delay.value).plus(difference).toMillis()
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
    private suspend fun runWithDuration(task: DurationRestTask, block: suspend () -> Unit) {
        GlobalScope.launch {
            while (isActive) {
                delay(task.delay.value)
                runTask(task, block)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun runTask(task: AbstractTask, block: suspend () -> Unit) {
        logger.info { "Start new run task cycle: $task" }
        block()
        GlobalScope.launch {
            taskRepository.updateById(task.id!!)
        }
        logger.info { "End success new run task cycle: $task" }
    }
}