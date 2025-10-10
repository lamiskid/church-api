package com.church.payload.chat

import com.church.model.chat.ChatRoomType
import java.util.UUID

data class CreateChatRoomRequest(
    val name: String,
    val type: ChatRoomType = ChatRoomType.GROUP,
    val participantIds: List<UUID> = emptyList()
)

