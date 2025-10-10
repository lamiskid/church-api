package com.church.payload.chat

import com.church.model.chat.ChatRoomType

data class ChatRoomResponse(
    val id: Long,
    val name: String,
    val type: ChatRoomType,
    val channelId: String,
    val participants: List<UserSummary>
)
