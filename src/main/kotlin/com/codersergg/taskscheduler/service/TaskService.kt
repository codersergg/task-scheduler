package com.codersergg.taskscheduler.service

import com.codersergg.taskscheduler.model.TaskRequestToCreate
import com.codersergg.taskscheduler.model.TaskRequestToUpdate
import com.codersergg.taskscheduler.model.TaskResponse
import com.codersergg.taskscheduler.repository.OwnerRepository
import com.codersergg.taskscheduler.repository.TaskRepository
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class TaskService(
    private val ownerRepository: OwnerRepository,
    private val taskRepository: TaskRepository
) {

    fun getAllTasks(): List<TaskResponse> {
        return taskRepository.findAllByOrderById().map { it.toTaskResponse() }
    }

    fun getTask(id: Long): TaskResponse {
        return taskRepository.findById(id).get().toTaskResponse()
    }

    fun createTask(task: TaskRequestToCreate): TaskResponse {
        val owner = ownerRepository.findByIdOrNull(task.owner.id) ?: throw NotFoundException()
        return taskRepository.save(task.toTask(owner)).toTaskResponse()
    }

    fun updateTask(task: TaskRequestToUpdate): Int {
        return taskRepository.update(task.id, task.lastRun)
    }

    fun deleteTaskById(id: Long) {
        return taskRepository.deleteById(id)
    }
}