package com.church.payload.devotiona

import java.time.Instant


data class DevotionalResponse(
    val id: Long,
    val title: String,
    val content: String,
    val scripture: String,
    val date: Instant,
    val bookmarked: Boolean = false
)
