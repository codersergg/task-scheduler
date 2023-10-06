package com.codersergg.taskscheduler.service

import com.codersergg.taskscheduler.model.OwnerRequestToAdd
import com.codersergg.taskscheduler.model.OwnerResponse
import com.codersergg.taskscheduler.model.OwnerResponseWithTask
import com.codersergg.taskscheduler.repository.Pagination
import com.codersergg.taskscheduler.repository.RequestParameters
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class OwnerServiceTest(@Autowired val ownerService: OwnerService) {
    @Nested
    @DisplayName("getAllOwners()")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetAllOwners {

        @Test
        fun `should return List of Owners`() {
            // when
            val allOwners = ownerService.getAllOwners(RequestParameters())

            // then
            Assertions.assertThat(allOwners.size).isEqualTo(3)
        }

        @Test
        fun `should return List of OwnersWithTask`() {
            // when
            val allOwners = ownerService.getAllOwnersWithTask(RequestParameters())

            // then
            Assertions.assertThat(allOwners.size).isEqualTo(3)
        }

        @Test
        fun `should return List of Owners containing 2 elements from the first`() {
            // when
            val allOwners = ownerService.getAllOwners(RequestParameters(
                pagination = Pagination(0, 2)
            ))

            // then
            Assertions.assertThat(allOwners.size).isEqualTo(2)
            Assertions.assertThat(allOwners[0].id).isEqualTo(1)
            Assertions.assertThat(allOwners[0].name).isEqualTo("task name 1")
            Assertions.assertThat(allOwners[1].id).isEqualTo(2)
            Assertions.assertThat(allOwners[1].name).isEqualTo("task name 2")
        }

        @Test
        fun `should return List of Owners containing 2 elements from the second`() {
            // when
            val allOwners = ownerService.getAllOwners(RequestParameters(
                pagination = Pagination(0, 2)
            ))

            // then
            Assertions.assertThat(allOwners.size).isEqualTo(2)
            Assertions.assertThat(allOwners[0].id).isEqualTo(1)
            Assertions.assertThat(allOwners[0].name).isEqualTo("task name 1")
            Assertions.assertThat(allOwners[1].id).isEqualTo(2)
            Assertions.assertThat(allOwners[1].name).isEqualTo("task name 2")
        }

        @Test
        fun `should return List of OwnersWithTask containing 1 elements from the first`() {
            // when
            val allOwners = ownerService.getAllOwnersWithTask(RequestParameters(
                pagination = Pagination(1, 2)
            ))

            // then
            Assertions.assertThat(allOwners.size).isEqualTo(1)
            Assertions.assertThat(allOwners[0].id).isEqualTo(1)
            Assertions.assertThat(allOwners[0].name).isEqualTo("task name 1")
        }

        @Test
        fun `should return List of OwnersWithTask containing 1 elements from the second`() {
            // when
            val allOwners = ownerService.getAllOwnersWithTask(RequestParameters(
                pagination = Pagination(1, 2)
            ))

            // then
            Assertions.assertThat(allOwners.size).isEqualTo(1)
            Assertions.assertThat(allOwners[0].id).isEqualTo(1)
            Assertions.assertThat(allOwners[0].name).isEqualTo("task name 1")
        }
    }

    @Nested
    @DisplayName("getOwner(id: Long)")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetOwnerById {

        @Test
        fun `should return Owner`() {
            // when
            val owner = ownerService.getOwner(1)

            // then
            Assertions.assertThat(owner).isInstanceOf(OwnerResponse::class.java)
            Assertions.assertThat(owner.id).isEqualTo(1)
        }

        @Test
        fun `should return OwnerWithTask`() {
            // when
            val owner = ownerService.getOwnerWithTasks(2)

            // then
            Assertions.assertThat(owner).isInstanceOf(OwnerResponseWithTask::class.java)
            Assertions.assertThat(owner.id).isEqualTo(2)
        }
    }

    /*@Nested
    @DisplayName("createOwner()")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class CreateOwner {

        @Test
        fun `should return Owner`() {
            // when
            val owner = ownerService.createOwner(OwnerRequestToAdd("new task name"))

            // then
            Assertions.assertThat(owner).isInstanceOf(OwnerResponse::class.java)
            Assertions.assertThat(owner.id).isEqualTo(1)
        }
    }*/
}