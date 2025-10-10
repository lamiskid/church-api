package com.church.repository

import com.church.model.chat.Message
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface MessageRepository : JpaRepository<Message, Long> {
    fun findAllByChatRoomIdOrderByCreatedAtAsc(chatRoomId: Long): List<Message>
}