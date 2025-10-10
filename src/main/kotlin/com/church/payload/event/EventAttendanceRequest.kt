package com.church.payload.event

import java.util.UUID

data class EventAttendanceRequest(
    val eventId: Long,
    val userId: UUID,
    val status: String
)