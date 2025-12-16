package com.church.payload.chat

import com.church.model.chat.ChatRoomType
import java.util.*

class CreateChatRoomResponse (
    val name: String,
    val description:String?,
    val type: ChatRoomType = ChatRoomType.GROUP,
    val participantIds: List<UUID> = emptyList()
)