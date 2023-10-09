package com.codersergg.taskscheduler.scheduler

import kotlinx.coroutines.*
import kotlin.time.Duration

object Scheduler {

    /**
     *
     * @params [task] lambdas expressions
     *
     * @params [duration] The format of string returned by the default Duration.toString and
     * toString in a specific unit, e.g. 10s, 1h 30m or -(1h 30m)
     *
     * Examples:
     *  val isoFormatString = "PT1H30M"
     *  val defaultFormatString = "1h 30m"
     *  val singleUnitFormatString = "1.5h"
     *  val invalidFormatString = "1 hour 30 minutes"
     *  println(Duration.parse(isoFormatString)) // 1h 30m
     *  println(Duration.parse(defaultFormatString)) // 1h 30m
     *  println(Duration.parse(singleUnitFormatString)) // 1h 30m
     *  // Duration.parse(invalidFormatString) //  will fail
     *  println(Duration.parseOrNull(invalidFormatString)) // null
     *
     */

    @OptIn(DelicateCoroutinesApi::class)
    fun run(task: suspend () -> Unit, duration: Duration) {
        GlobalScope.launch {
            while (isActive) {
                delay(duration)
                task()
            }
        }
    }
}