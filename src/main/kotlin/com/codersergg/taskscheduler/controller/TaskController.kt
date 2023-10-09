package com.codersergg.taskscheduler.controller

import com.codersergg.taskscheduler.dto.request.TaskToCreateRequest
import com.codersergg.taskscheduler.dto.request.TaskToUpdateRequest
import com.codersergg.taskscheduler.dto.response.TaskResponseWithTask
import com.codersergg.taskscheduler.service.TaskService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/task")
class TaskController(private val taskService: TaskService) {
    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(e: NoSuchElementException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(e: IllegalArgumentException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.BAD_REQUEST)

    @GetMapping
    fun getAllTask(): List<TaskResponseWithTask> = taskService.getAllTasks()

    @GetMapping("/{id}")
    fun getTask(@PathVariable id: Long): TaskResponseWithTask = taskService.getTask(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createTask(@RequestBody task: TaskToCreateRequest): TaskResponseWithTask = taskService.createTask(task)

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    fun updateTask(@RequestBody task: TaskToUpdateRequest): Int = taskService.updateTask(task)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteTaskById(@PathVariable id: Long): Unit = taskService.deleteTaskById(id)
}