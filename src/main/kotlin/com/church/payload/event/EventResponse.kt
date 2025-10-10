package com.church.payload.event

import java.time.LocalDate
import java.time.LocalTime

data class EventResponse(
    val id: Long,
    val title: String,
    val description: String,
    val eventDate: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val location: String,
    val createdByName: String
)