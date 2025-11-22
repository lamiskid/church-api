package com.church.payload.sermon

import java.time.LocalDateTime
import java.util.UUID

data class SermonResponse(
    val id: UUID?,
    val title: String,
    val content: String,
    val preacher: String,
    val createdBy: UUID,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
)