package com.church.payload.chat

import java.util.UUID

data class MessageResponse(
    val id: Long,
    val chatRoomId: Long,
    val senderId: UUID,
    val senderName: String,
    val content: String,
    val createdAt: Long
)
