package com.codersergg.taskscheduler.service

import com.codersergg.taskscheduler.dto.request.TaskToCreateRequest
import com.codersergg.taskscheduler.dto.request.TaskToUpdateRequest
import com.codersergg.taskscheduler.dto.response.TaskResponseWithDelay
import com.codersergg.taskscheduler.repository.ProviderRepository
import com.codersergg.taskscheduler.repository.TaskRepository
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class TaskService(
    private val providerRepository: ProviderRepository,
    private val taskRepository: TaskRepository
) {

    fun getAllTasks(): List<TaskResponseWithDelay> {
        return taskRepository.findAllByOrderById().map { it.toTaskResponseWithDelay() }
    }

    fun getTask(id: Long): TaskResponseWithDelay {
        return taskRepository.findById(id).get().toTaskResponseWithDelay()
    }

    fun createTask(task: TaskToCreateRequest): TaskResponseWithDelay {
        val provider = providerRepository.findByIdOrNull(task.provider.id) ?: throw NotFoundException()
        return taskRepository.save(task.toTask(provider)).toTaskResponseWithDelay()
    }

    fun updateTask(task: TaskToUpdateRequest): Int {
        return taskRepository.update(task.id, task.lastRun)
    }

    fun deleteTaskById(id: Long) {
        return taskRepository.deleteById(id)
    }
}