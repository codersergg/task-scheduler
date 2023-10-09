package com.codersergg.taskscheduler.controller

import com.codersergg.taskscheduler.scheduler.Scheduler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.time.Duration

@RestController
@RequestMapping("api/scheduler")
class SchedulerController {


    @GetMapping
    fun startTask(): Boolean {
        Scheduler.run({ println("Task Run") }, Duration.parse("1s"))
        return true
    }
}