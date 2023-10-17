package com.codersergg.taskscheduler.controller

import com.codersergg.taskscheduler.dto.RestTask
import com.codersergg.taskscheduler.repository.TaskRepository
import com.codersergg.taskscheduler.scheduler.Scheduler
import com.codersergg.taskscheduler.util.SchedulerHttpClient
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.http.HttpMethod
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("api/scheduler")
class SchedulerController(
    private val taskRepository: TaskRepository,
    private val scheduler: Scheduler,
) {

    @OptIn(DelicateCoroutinesApi::class)
    @PostMapping
    fun startTask(@RequestBody restTask: RestTask): Boolean {
        val createTask = taskRepository.createTask(restTask.toTask())
        restTask.id = createTask.id
        val url = restTask.pathResponse.path
        GlobalScope.launch {
            scheduler.run(
                { SchedulerHttpClient.sendRequest(url as URI, HttpMethod.POST, body = restTask) },
                restTask
            )
        }
        return true
    }
}