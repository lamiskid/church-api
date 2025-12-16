package com.church.payload.devotional

data class DevotionalRequest(
    val title: String,
    val content: String,
    val scripture: String,
)
