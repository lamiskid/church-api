package com.church.repository

import com.church.model.event.Event
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface EventRepository : JpaRepository<Event, Long> {
    fun findByCreatedById(userId: UUID): List<Event>
}