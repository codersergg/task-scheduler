package com.codersergg.taskscheduler.service

import com.codersergg.taskscheduler.dto.request.TaskToUpdateRequest
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
            provider = Provider(name = task.provider.name)
            session.persist(provider)
        }
        task.provider = provider
        session.persist(task)
        return task.toTaskResponseWithDelay()
    }

    fun updateTask(task: TaskToUpdateRequest): Int {
        return taskRepository.update(task.id, task.lastRun)
    }

    fun deleteTaskById(id: Long) {
        return taskRepository.deleteById(id)
    }
}