package com.church.payload.chat

import java.util.UUID


data class ChatMembersDetails(
    val id: UUID? = null,
    val name: String,
    val imageUrl: String? = null
)