package com.church.controller

import com.church.payload.chat.AddParticipantRequest
import com.church.payload.chat.ChatRoomResponse
import com.church.payload.chat.CreateChatRoomRequest
import com.church.payload.chat.MessageRequest
import com.church.payload.chat.MessageResponse
import com.church.security.User
import com.church.service.ChatService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/chat")
class ChatController(
    private val chatService: ChatService,
) {

    @PostMapping("/rooms")
    fun createChatRoom(
        @AuthenticationPrincipal user: User,
        @RequestBody request: CreateChatRoomRequest
    ): ResponseEntity<ChatRoomResponse> {
        val response = chatService.createChatRoom(request, user)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/rooms/{roomId}/participants")
    fun addParticipants(
        @PathVariable roomId: Long,
        @RequestBody request: AddParticipantRequest
    ): ResponseEntity<Void> {
        chatService.addParticipants(roomId, request)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/rooms")
    fun getUserRooms(
        @AuthenticationPrincipal user: User
    ): ResponseEntity<List<ChatRoomResponse>> {
        val rooms = chatService.listUserChatRooms(user)
        return ResponseEntity.ok(rooms)
    }

    @PostMapping
    fun sendMessage(
        @AuthenticationPrincipal user: User,
        @RequestBody request: MessageRequest
    ): ResponseEntity<MessageResponse> {
        val response = chatService.sendMessage(request, user)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{chatRoomId}")
    fun getMessages(@PathVariable chatRoomId: Long): ResponseEntity<List<MessageResponse>> {
        val responses = chatService.getMessages(chatRoomId)
        return ResponseEntity.ok(responses)
    }
}
