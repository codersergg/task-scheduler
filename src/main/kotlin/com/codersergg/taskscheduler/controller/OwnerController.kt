package com.codersergg.taskscheduler.controller

import com.codersergg.taskscheduler.model.OwnerResponse
import com.codersergg.taskscheduler.model.OwnerResponseWithTask
import com.codersergg.taskscheduler.repository.RequestParameters
import com.codersergg.taskscheduler.service.OwnerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
@RestController
@RequestMapping("api/owner")
class OwnerController(private val ownerService: OwnerService) {
    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(e: NoSuchElementException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(e: IllegalArgumentException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.BAD_REQUEST)

    @GetMapping
    fun getAllOwner(@RequestBody params: RequestParameters?): List<OwnerResponse> {
        return ownerService.getAllOwners(params ?: RequestParameters())
    }

    @GetMapping("/task")
    fun getAllOwnerWithTasks(@RequestBody params: RequestParameters?): List<OwnerResponseWithTask> {
        return ownerService.getAllOwnersWithTask(params ?: RequestParameters())
    }

    @GetMapping("/{id}")
    fun getOwner(@PathVariable id: Long): OwnerResponse = ownerService.getOwner(id)

    @GetMapping("/{id}/task")
    fun getOwnerWithTask(@PathVariable id: Long): OwnerResponseWithTask = ownerService.getOwnerWithTasks(id)
}

