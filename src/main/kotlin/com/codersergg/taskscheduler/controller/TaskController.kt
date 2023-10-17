package com.codersergg.taskscheduler.controller

import com.codersergg.taskscheduler.dto.TaskToUpdateRequest
import com.codersergg.taskscheduler.dto.response.TaskResponseWithDelay
import com.codersergg.taskscheduler.model.Task
import com.codersergg.taskscheduler.repository.TaskRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/task")
class TaskController(private val taskRepository: TaskRepository) {
    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(e: NoSuchElementException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(e: IllegalArgumentException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.BAD_REQUEST)

    @GetMapping
    fun getAllTask(): List<Task> = taskRepository.getAllTasks()

    @GetMapping("/{id}")
    fun getTask(@PathVariable id: Long): Task = taskRepository.getTask(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createTask(@RequestBody task: Task): TaskResponseWithDelay = taskRepository.createTask(task)

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    fun updateTask(@RequestBody task: TaskToUpdateRequest): Int = taskRepository.updateTask(task)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteTaskById(@PathVariable id: Long): Unit = taskRepository.deleteTaskById(id)
}