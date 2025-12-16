
package com.church.service

import com.church.exception.ResourceNotFoundException
import com.church.model.account.Account
import com.church.model.account.UserRole
import com.church.model.chat.ChatRoom
import com.church.model.chat.Message
import com.church.payload.chat.*
import com.church.payload.pagination.PageResponse
import com.church.payload.pagination.PaginationMapper
import com.church.payload.profile.UserProfileDetails
import com.church.repository.AccountRepository
import com.church.repository.ChatRoomRepository
import com.church.repository.MessageRepository
import com.church.security.User
import com.church.util.FcmServiceUtil
import org.springframework.data.domain.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID


@Service
class ChatService(
    private val accountRepository: AccountRepository,
    private val chatRoomRepository: ChatRoomRepository,
    private val messageRepository: MessageRepository,
    private val centrifugoService: CentrifugoService,
    private val fcmServiceUtil: FcmServiceUtil
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
       // val response = savedMessage.toResponse()

     /*   val event = CentrifugoEvent(
            type = "NEW_MESSAGE",
            payload = MessagePayload(
                id = savedMessage.id,
                chatRoomId = chatRoom.id,
                senderId = account.id!!,
                senderName = account.username,
                content = savedMessage.content,
                createdAt = savedMessage.createdAt
            )
        )
        val event2 = MessagePayload(
            id = savedMessage.id,
            chatRoomId = chatRoom.id,
            senderId = account.id!!,
            senderName = account.username,
            content = savedMessage.content,
            createdAt = savedMessage.createdAt
        )*/
        //centrifugoService.publish(/*chatRoom.channelId*/"channel", event2)
     /*   fcmServiceUtil.sendPushNotification(
            fcmToken = "c7ptNxt2RNKLdq2OxPFHvZ:APA91bF6FMrvZ95fggllsBQwDcmqbQ14c8-69mpz0bXtmAaF5Pl1N3GvF7KOr4QrOyDNylbNNKjSIVAYyqabALtnFK2MBDD221cal0s2_vmT3PYKz09VmzY",
            title = "New message from ${account.username}",
            body = "Hello",
            data = mapOf("eventType" to "NEW_MESSAGE","eventRouteId" to chatRoom.id.toString(),)
        )*/
        return MessageResponse(
            id =message.id,
            chatRoomId = message.chatRoom.id,
            senderName = account.email,
            senderId = null,
            createdAt = message.createdAt,
            content = message.content
        )
    }

    fun getMessages(chatRoomId: Long): List<MessageResponse> {
        val messages = messageRepository.findAllByChatRoomIdOrderByCreatedAtAsc(chatRoomId)
        return messages.map { it.toResponse() }
    }

    fun getMessagesV2(chatRoomId: Long, pageable: Pageable): Page<MessageResponse> {
        val messages = messageRepository.findAllByChatRoomIdOrderByCreatedAtAsc(chatRoomId,pageable)
        return messages.map { it.toResponse() }
    }


    @Transactional
    fun createChatRoom(request: CreateChatRoomRequest, user: User ): CreateChatRoomResponse {
        val participants = mutableSetOf<Account>().apply {
            add(accountRepository.getReferenceById(user.getId()))
            addAll(accountRepository.findAllById(request.participantIds))
        }

        val chatRoom = ChatRoom(
            name = request.name,
            type = request.type,
            channelId = UUID.randomUUID().toString(),
            description = request.description,
            participants = participants,
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

        return CreateChatRoomResponse(
            saved.name,
            saved.description,
            saved.type,
        )
    }
    fun getAllUsersForChatRoom(user: User, page: Int, size: Int): PageResponse<UserProfileDetails> {

        val pageable: Pageable = PageRequest.of(page, size)

        val pageResult = accountRepository.findAll(pageable)

       // val filteredAccounts = pageResult.content.filter { it.id != user.getId() }

        return PaginationMapper.toPageResponse(pageResult) { account ->
            UserProfileDetails(
                userId = account.id!!,
                firstName = account.profile?.firstName ?: "",
                lastName = account.profile?.lastName ?: "",
                profilePictureUrl = account.profile?.profilePictureUrl,
                role = account.userRoles.map { userRole -> userRole.roleType },
            )
        }
    }



    @Transactional
    fun addParticipants(roomId: Long, request: AddParticipantRequest) {
        val room = chatRoomRepository.findById(roomId)
            .orElseThrow { IllegalArgumentException("Chat room not found") }

        val account = accountRepository.findAllById(request.accountIds)
        room.participants.addAll(account)
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

    fun listUserChatRooms(
        user: User,
        page: Int,
        size: Int
    ): Page<ChatRoomResponse> {

        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"))

        val roomsPage = chatRoomRepository.findAllByParticipantsId(
            user.getId(),
            pageable
        )

        val roomIds = roomsPage.content.map { it.id }

        val latestMessages = messageRepository.findLatestMessagesByChatRoomIds(roomIds)
            .associateBy { it.chatRoom.id }

        val roomResponses = roomsPage.content.map { room ->
            val lastMessage = latestMessages[room.id]
            room.toResponse(lastMessage)
        }

        return PageImpl(roomResponses, pageable, roomsPage.totalElements)
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


