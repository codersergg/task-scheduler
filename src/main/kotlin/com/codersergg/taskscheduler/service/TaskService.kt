package com.codersergg.taskscheduler.service

import com.codersergg.taskscheduler.dto.TaskToUpdateRequest
import com.codersergg.taskscheduler.dto.response.TaskResponseWithDelay
import com.codersergg.taskscheduler.model.Provider
import com.codersergg.taskscheduler.model.Task
import com.codersergg.taskscheduler.repository.ProviderRepository
import com.codersergg.taskscheduler.repository.TaskRepository
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.hibernate.Session
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TaskService(
    @PersistenceContext private val em: EntityManager,
    private val providerRepository: ProviderRepository,
    private val taskRepository: TaskRepository
) {

    fun getAllTasks(): List<TaskResponseWithDelay> {
        return taskRepository.findAllByOrderById().map { it.toTaskResponseWithDelay() }
    }

    fun getTask(id: Long): TaskResponseWithDelay {
        return taskRepository.findById(id).get().toTaskResponseWithDelay()
    }

    @Transactional
    fun createTask(task: Task): TaskResponseWithDelay {
        val name = task.provider.name
        val session = em.unwrap(Session::class.java)
        var provider =
            session.createSelectionQuery("select p from Provider p where p.name like (:name)", Provider::class.java)
                .setParameter("name", name).singleResultOrNull
        if (provider == null) {
            provider = Provider(name = task.provider.name, type = task.provider.type)
            session.persist(provider)
        }
        task.provider = provider
        session.persist(task)
        return task.toTaskResponseWithDelay()
    }

    fun updateTask(task: TaskToUpdateRequest): Int {
        val findByName = providerRepository.findByName(task.provider.name)
        val taskOptional = taskRepository.findById(task.id)
        if (!taskOptional.isPresent) throw IllegalArgumentException()

        if (findByName.isPresent && task.provider.name == taskOptional.get().provider.name) {
            println("task.provider.name: ${task.provider.name}")
            println("findByName.get().name: ${findByName.get().name}")
            return taskRepository.update(task.id, task.lastRun)
        } else throw IllegalArgumentException()
    }

    fun deleteTaskById(id: Long) {
        return taskRepository.deleteById(id)
    }
}