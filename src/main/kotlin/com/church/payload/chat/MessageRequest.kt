package com.church.payload.chat

data class MessageRequest(
    val chatRoomId: Long,
    val content: String
)
