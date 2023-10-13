package com.codersergg.taskscheduler.controller

import com.codersergg.taskscheduler.dto.TaskToUpdateRequest
import com.codersergg.taskscheduler.dto.response.TaskResponseWithDelay
import com.codersergg.taskscheduler.model.Task
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
    fun getAllTask(): List<TaskResponseWithDelay> = taskService.getAllTasks()

    @GetMapping("/{id}")
    fun getTask(@PathVariable id: Long): TaskResponseWithDelay = taskService.getTask(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createTask(@RequestBody task: Task): TaskResponseWithDelay = taskService.createTask(task)

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    fun updateTask(@RequestBody task: TaskToUpdateRequest): Int = taskService.updateTask(task)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteTaskById(@PathVariable id: Long): Unit = taskService.deleteTaskById(id)
}