package com.codersergg.taskscheduler.controller

import com.codersergg.taskscheduler.dto.response.ProviderResponse
import com.codersergg.taskscheduler.dto.response.ProviderWithTaskResponse
import com.codersergg.taskscheduler.repository.Pagination
import com.codersergg.taskscheduler.repository.RequestParameters
import com.codersergg.taskscheduler.repository.ProviderRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/provider")
class ProviderController(private val providerRepository: ProviderRepository) {
    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(e: NoSuchElementException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(e: IllegalArgumentException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.BAD_REQUEST)

    @GetMapping
    fun getAllProvider(@RequestParam firstResult: Int?, @RequestParam maxResult: Int?): List<ProviderResponse> {
        return providerRepository.getAllProviders(RequestParameters(Pagination(firstResult ?: 0, maxResult ?: 20)))
    }

    @PostMapping
    fun getAllProvider(@RequestBody params: RequestParameters?): List<ProviderResponse> {
        return providerRepository.getAllProviders(params ?: RequestParameters())
    }

    @GetMapping("/task")
    fun getAllProviderWithTasks(
        @RequestParam firstResult: Int?,
        @RequestParam maxResult: Int?
    ): List<ProviderWithTaskResponse> {
        return providerRepository.getAllProvidersWithTask(RequestParameters(Pagination(firstResult ?: 0, maxResult ?: 20)))
    }

    @PostMapping("/task")
    fun getAllProviderWithTasks(@RequestBody params: RequestParameters?): List<ProviderWithTaskResponse> {
        println("params: $params")
        return providerRepository.getAllProvidersWithTask(params ?: RequestParameters())
    }

    @GetMapping("/{id}")
    fun getProvider(@PathVariable id: Long): ProviderResponse = providerRepository.getProvider(id)

    @GetMapping("/{id}/task")
    fun getProviderWithTask(@PathVariable id: Long): ProviderWithTaskResponse = providerRepository.getProviderWithTasks(id)
}

