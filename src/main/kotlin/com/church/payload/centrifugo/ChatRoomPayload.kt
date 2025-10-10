package com.church.payload.centrifugo

import com.church.model.chat.ChatRoomType
import com.church.payload.chat.UserSummary


data class ChatRoomPayload(
    val id: Long,
    val name: String,
    val channelId: String,
    val type: ChatRoomType,
    val participants: List<UserSummary>
)