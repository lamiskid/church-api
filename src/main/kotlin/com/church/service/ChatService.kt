
package com.church.service

import com.church.exception.ResourceNotFoundException
import com.church.model.account.Account
import com.church.model.chat.ChatRoom
import com.church.model.chat.Message
import com.church.payload.centrifugo.CentrifugoEvent
import com.church.payload.centrifugo.MessagePayload
import com.church.payload.chat.AddParticipantRequest
import com.church.payload.chat.ChatRoomResponse
import com.church.payload.chat.CreateChatRoomRequest
import com.church.payload.chat.MessageRequest
import com.church.payload.chat.MessageResponse
import com.church.payload.chat.toResponse
import com.church.repository.AccountRepository
import com.church.repository.ChatRoomRepository
import com.church.repository.MessageRepository
import com.church.security.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID


@Service
class ChatService(
    private val accountRepository: AccountRepository,
    private val chatRoomRepository: ChatRoomRepository,
    private val messageRepository: MessageRepository,
    private val centrifugoService: CentrifugoService
) {

    @Transactional
    fun sendMessage(request: MessageRequest, user: User): MessageResponse {
        val chatRoom = chatRoomRepository.findById(request.chatRoomId)
            .orElseThrow { IllegalArgumentException("Chat room not found") }

        val account =accountRepository.findById(user.getId())
            .orElseThrow { ResourceNotFoundException("User not found") }

        val message = Message(
            chatRoom = chatRoom,
            sender =  account,
            content = request.content
        )

        val savedMessage = messageRepository.save(message)
        val response = savedMessage.toResponse()

        val event = CentrifugoEvent(
            type = "NEW_MESSAGE",
            payload = MessagePayload(
                id = savedMessage.id,
                chatRoomId = chatRoom.id,
                senderId = account.id,
                senderName = account.username,
                content = savedMessage.content,
                createdAt = savedMessage.createdAt
            )
        )
        val event2 = MessagePayload(
            id = savedMessage.id,
            chatRoomId = chatRoom.id,
            senderId = account.id,
            senderName = account.username,
            content = savedMessage.content,
            createdAt = savedMessage.createdAt
        )
        centrifugoService.publish(/*chatRoom.channelId*/"channel", event2)
        return response
    }

    fun getMessages(chatRoomId: Long): List<MessageResponse> {
        val messages = messageRepository.findAllByChatRoomIdOrderByCreatedAtAsc(chatRoomId)
        return messages.map { it.toResponse() }
    }


    @Transactional
    fun createChatRoom(request: CreateChatRoomRequest, user: User ): ChatRoomResponse {
        val participants = mutableSetOf<Account>().apply {
            add(accountRepository.getReferenceById(user.getId()))
            addAll(accountRepository.findAllById(request.participantIds))
        }

        val chatRoom = ChatRoom(
            name = request.name,
            type = request.type,
            channelId = UUID.randomUUID().toString(),
            participants = participants
        )

        val saved = chatRoomRepository.save(chatRoom)
        val response = saved.toResponse()

       /* val event = CentrifugoEvent(
            type = "ROOM_CREATED",
            payload = ChatRoomPayload(
                id = saved.id,
                name = saved.name,
                channelId = saved.channelId,
                type = saved.type,
                participants = participants.map { UserSummary(it.id, it.username) }
            )
        )
        centrifugoService.publish(saved.channelId, event)*/

        return response
    }

    @Transactional
    fun addParticipants(roomId: Long, request: AddParticipantRequest) {
        val room = chatRoomRepository.findById(roomId)
            .orElseThrow { IllegalArgumentException("Chat room not found") }

        val users = accountRepository.findAllById(request.userIds)
        room.participants.addAll(users)
        chatRoomRepository.save(room)
    }

    fun listUserChatRooms(user: User): List<ChatRoomResponse> {
        val rooms = chatRoomRepository.findAllByParticipantsId(user.getId())
        val roomIds = rooms.map { it.id }

        val latestMessages = messageRepository.findLatestMessagesByChatRoomIds(roomIds)
            .associateBy { it.chatRoom.id }

        return rooms.map { room ->
            val lastMessage = latestMessages[room.id]
            room.toResponse(lastMessage)
        }
    }



    /* fun listUserChatRooms(user: User): List<ChatRoomResponse> {
         val rooms = chatRoomRepository.findAllByParticipantsId(user.getId())
         return rooms.map { it.toResponse() }
     }*/

/*
    fun getAllChatRoomsForUser(userId: Long): List<ChatRoom> {
        return chatRoomRepository.findByParticipants_Id(userId)
    }

    fun getMessagesInChatRoom(chatRoomId: Long): List<Message> {
        return messageRepository.findByChatRoom_IdOrderByTimestampAsc(chatRoomId)
    }


    @Transactional
    fun joinGroupRoom(roomId: Long, userId: Long): ChatRoom {
        val room = chatRoomRepository.findById(roomId)
            .orElseThrow { IllegalArgumentException("Room not found") }

        if (room.type != ChatRoomType.GROUP) {
            throw IllegalArgumentException("Not a group chat")
        }

        val user = accountRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found") }

        val updatedRoom = room.copy(participants = room.participants + user)

        return chatRoomRepository.save(updatedRoom)
    }


    fun getOrCreatePrivateChatRoom(senderId: Long, receiverId: Long,roomId: Long): ChatRoom {
        //val existing = chatRoomRepository.findPrivateChatRoomBetween(senderId, receiverId)

        val existing= chatRoomRepository.findById(roomId).orElseThrow {
            IllegalArgumentException("chat room not found")
        }

        if (existing != null) return existing

        val users = accountRepository.findAllById(listOf(senderId, receiverId)).toSet()

        val newRoom = ChatRoom(
            name = "",
            type = ChatRoomType.PRIVATE,
            participants = users
        )

        return chatRoomRepository.save(newRoom)
    }
*/

}


