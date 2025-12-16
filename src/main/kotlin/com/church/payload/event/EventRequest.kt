package com.church.payload.event

import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

data class EventRequest(
    val title: String,
    val description: String,
    val eventDate: LocalDate = LocalDate.now(),
    val startTime: LocalTime = LocalTime.now(),
    val endTime: LocalTime = LocalTime.now(),
    val location: String,
)