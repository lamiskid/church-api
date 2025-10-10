package com.church.payload.centrifugo

import java.util.UUID

data class MessagePayload(
    val id: Long,
    val chatRoomId: Long,
    val senderId: UUID,
    val senderName: String,
    val content: String,
    val createdAt: Long
)