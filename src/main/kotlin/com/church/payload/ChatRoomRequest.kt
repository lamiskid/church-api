package com.church.payload

import com.church.model.chat.ChatRoomType


data class ChatRoomRequest(
    val name: String,
    val type: ChatRoomType = ChatRoomType.GROUP,
)
