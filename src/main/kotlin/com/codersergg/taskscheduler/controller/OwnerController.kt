package com.codersergg.taskscheduler.controller

import com.codersergg.taskscheduler.dto.response.OwnerResponse
import com.codersergg.taskscheduler.dto.response.OwnerWithTaskResponse
import com.codersergg.taskscheduler.repository.Pagination
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
    fun getAllOwner(@RequestParam firstResult: Int?, @RequestParam maxResult: Int?): List<OwnerResponse> {
        return ownerService.getAllOwners(RequestParameters(Pagination(firstResult ?: 0, maxResult ?: 20)))
    }

    @PostMapping
    fun getAllOwner(@RequestBody params: RequestParameters?): List<OwnerResponse> {
        return ownerService.getAllOwners(params ?: RequestParameters())
    }

    @GetMapping("/task")
    fun getAllOwnerWithTasks(
        @RequestParam firstResult: Int?,
        @RequestParam maxResult: Int?
    ): List<OwnerWithTaskResponse> {
        return ownerService.getAllOwnersWithTask(RequestParameters(Pagination(firstResult ?: 0, maxResult ?: 20)))
    }

    @PostMapping("/task")
    fun getAllOwnerWithTasks(@RequestBody params: RequestParameters?): List<OwnerWithTaskResponse> {
        println("params: $params")
        return ownerService.getAllOwnersWithTask(params ?: RequestParameters())
    }

    @GetMapping("/{id}")
    fun getOwner(@PathVariable id: Long): OwnerResponse = ownerService.getOwner(id)

    @GetMapping("/{id}/task")
    fun getOwnerWithTask(@PathVariable id: Long): OwnerWithTaskResponse = ownerService.getOwnerWithTasks(id)
}

