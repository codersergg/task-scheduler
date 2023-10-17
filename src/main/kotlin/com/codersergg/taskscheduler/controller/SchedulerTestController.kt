package com.codersergg.taskscheduler.controller

import com.codersergg.taskscheduler.dto.RestTask
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/test")
class SchedulerTestController {
    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleBadRequest(e: HttpMessageNotReadableException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.BAD_REQUEST)

    @PostMapping
    fun testRout(@RequestBody task: RestTask?): Boolean {
        logger.info { "Test response from Scheduler with Task: $task" }
        return true
    }
}