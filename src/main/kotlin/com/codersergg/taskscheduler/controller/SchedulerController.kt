package com.codersergg.taskscheduler.controller

import com.codersergg.taskscheduler.dto.RestTask
import com.codersergg.taskscheduler.scheduler.Scheduler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("api/scheduler")
class SchedulerController {

    @PostMapping
    fun startTask(@RequestBody restTask: RestTask): Boolean {
        Scheduler.run({ println("Task Run") }, restTask)
        return true
    }
}