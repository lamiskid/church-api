package com.church.payload.event

data class EventAttendanceResponse(
    val attendanceId: Long,
    val eventId: Long,
    val userName: String,
    val status: String
)