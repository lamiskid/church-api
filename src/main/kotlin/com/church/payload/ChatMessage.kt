package com.church.payload


data class ChatMessage(
    val chatRoomId: Long,
    val senderId: Long,
    val content: String
)
