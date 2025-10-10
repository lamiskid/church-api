package com.church.payload.event

import com.church.model.event.Event
import com.church.model.event.EventAttendance

fun Event.toResponse(): EventResponse =
    EventResponse(
        id = id,
        title = title,
        description = description,
        eventDate = eventDate,
        startTime = startTime,
        endTime = endTime,
        location = location,
        createdByName = createdBy.username
    )

fun EventAttendance.toResponse(): EventAttendanceResponse =
    EventAttendanceResponse(
        attendanceId = id,
        eventId = event.id,
        userName = user.username,
        status = status.name
    )