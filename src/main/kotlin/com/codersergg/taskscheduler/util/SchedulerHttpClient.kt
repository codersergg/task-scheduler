package com.codersergg.taskscheduler.util

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import java.net.URI

object SchedulerHttpClient {
    private val restTemplate = RestTemplate()

    fun sendRequest(
        url: URI,
        method: HttpMethod,
        headers: HttpHeaders = HttpHeaders(),
        body: Any? = null
    ): ResponseEntity<*> {
        val request = HttpEntity(body, headers)
        return restTemplate.exchange(url, method, request, Any::class.java)
    }
}