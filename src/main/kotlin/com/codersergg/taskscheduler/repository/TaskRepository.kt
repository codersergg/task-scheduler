package com.codersergg.taskscheduler.repository

import com.codersergg.taskscheduler.configuration.ApplicationProperties
import com.codersergg.taskscheduler.dto.TaskToUpdateRequest
import com.codersergg.taskscheduler.dto.response.TaskResponseWithDelay
import com.codersergg.taskscheduler.model.Provider
import com.codersergg.taskscheduler.model.Task
import com.codersergg.taskscheduler.model.Task_
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.persistence.criteria.CriteriaUpdate
import org.hibernate.Session
import org.hibernate.query.criteria.HibernateCriteriaBuilder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class TaskRepository(
    @PersistenceContext private val em: EntityManager,
    private val providerRepositoryJpa: ProviderRepositoryJpa,
    private val taskRepositoryJpa: TaskRepositoryJpa,
    private val applicationProperties: ApplicationProperties
) {

    private val session: Session = em.unwrap(Session::class.java)
    fun getAllTasks(): List<Task> {
        return taskRepositoryJpa.findAllByOrderById()
    }

    fun getAllTasksNotRun(): List<Task> {
        val criteriaBuilder = session.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(Task::class.java)
        val root = criteriaQuery.from(Task::class.java)
        val lastRun = root.get(Task_.lastRun)
        criteriaQuery.where(
            criteriaBuilder.lessThan(
                lastRun,
                Instant.now().minusMillis(applicationProperties.updateTime.toLong() * 2)
            )
        )
        val query = session.createQuery(criteriaQuery)
        val graph = session.createEntityGraph(Task::class.java)
        graph.addSubgraph(Task_.provider)
        query.setHint("jakarta.persistence.fetchgraph", graph)
        return query.resultList
    }

    fun getTask(id: Long): Task {
        return taskRepositoryJpa.findById(id).get()
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

    @Transactional
    fun updateById(taskId: Long): Int {
        val builder: HibernateCriteriaBuilder = session.criteriaBuilder
        val update: CriteriaUpdate<Task> = builder.createCriteriaUpdate(Task::class.java)
        val root = update.from(Task::class.java)
        update.where(builder.equal(root[Task_.id], taskId))
        update.set(Task_.lastRun, Instant.now())
        val executeUpdate = session.createMutationQuery(update).executeUpdate()
        session.flush()
        session.clear()
        return executeUpdate
    }

    fun updateTask(task: TaskToUpdateRequest): Int {
        val findByName = providerRepositoryJpa.findByName(task.provider.name)
        val taskOptional = taskRepositoryJpa.findById(task.id!!)
        if (!taskOptional.isPresent) throw IllegalArgumentException()

        if (findByName.isPresent && task.provider.name == taskOptional.get().provider.name) {
            println("task.provider.name: ${task.provider.name}")
            println("findByName.get().name: ${findByName.get().name}")
            return taskRepositoryJpa.update(task.id!!, task.lastRun)
        } else throw IllegalArgumentException()
    }

    fun deleteTaskById(id: Long) {
        return taskRepositoryJpa.deleteById(id)
    }
}