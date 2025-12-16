package com.church.payload.chat

import com.church.model.chat.ChatRoom
import com.church.model.chat.Message

fun Message.toResponse(): MessageResponse =
    MessageResponse(
        id = id,
        chatRoomId = chatRoom.id,
        senderId = sender.id!!,
        senderName = sender.email,
        content = content,
        createdAt = createdAt
    )


fun ChatRoom.toResponse(): ChatRoomResponse =
    ChatRoomResponse(
        id = id,
        name = name,
        type = type,
        channelId = channelId,
        participants = participants.map { ChatMembersDetails(requireNotNull( it.id), it.email) },
        description = description?:"",
        lastMessage = null
    )

fun ChatRoom.toResponse(lastMessage: Message?): ChatRoomResponse {
    return ChatRoomResponse(
        id = id,
        name = name,
        type = type,
        description = description?:"",
        channelId = channelId,
        participants = participants.map { ChatMembersDetails(it.id, it.email) },
        lastMessage = lastMessage?.let {
            MessageResponse(
                id = it.id,
                senderId = it.sender.id,
                content = it.content,
                createdAt = it.createdAt,
                chatRoomId = id,
                senderName = it.sender.email
            )
        }
    )
}
