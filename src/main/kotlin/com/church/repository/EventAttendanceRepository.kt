package com.church.repository

import com.church.model.event.EventAttendance
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface EventAttendanceRepository : JpaRepository<EventAttendance, Long> {
    fun findByEventId(eventId: Long): List<EventAttendance>
    fun findByUserId(userId: UUID): List<EventAttendance>
}