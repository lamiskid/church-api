package com.church.payload.chat

import java.util.UUID


data class UserSummary(
    val id: UUID,
    val name: String,
    val imageUrl: String? = null
)