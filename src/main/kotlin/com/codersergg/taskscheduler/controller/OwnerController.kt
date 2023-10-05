package com.codersergg.taskscheduler.controller

import com.codersergg.taskscheduler.model.OwnerResponse
import com.codersergg.taskscheduler.model.OwnerResponseGraph
import com.codersergg.taskscheduler.model.OwnerResponseLazy
import com.codersergg.taskscheduler.service.OwnerService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/owner")
class OwnerController(private val ownerService: OwnerService) {
    @GetMapping
    fun getAllOwner(): List<OwnerResponse> = ownerService.getAllOwners()
    @GetMapping("/graph")
    fun getAllOwnerGraph(): List<OwnerResponseGraph> = ownerService.getAllOwnersGraph()

    @GetMapping("/{id}")
    fun getOwnerH(@PathVariable id: Long): OwnerResponseLazy = ownerService.getOwner(id)

    @GetMapping("/{id}/full")
    fun getOwner(@PathVariable id: Long): OwnerResponseGraph = ownerService.getOwnerWithTasks(id)
}