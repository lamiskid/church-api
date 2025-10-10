package com.church.payload.event

import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

data class EventRequest(
    val title: String,
    val description: String,
    val eventDate: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val location: String,
    val createdById: UUID
)