package com.church.repository

import com.church.model.chat.Message
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository


@Repository
interface MessageRepository : JpaRepository<Message, Long> {
    fun findAllByChatRoomIdOrderByCreatedAtAsc(chatRoomId: Long): List<Message>

    @Query("""
    SELECT m FROM Message m
    WHERE m.chatRoom.id IN :chatRoomIds
    AND m.createdAt = (
        SELECT MAX(m2.createdAt)
        FROM Message m2
        WHERE m2.chatRoom.id = m.chatRoom.id
    )"""
    )
    fun findLatestMessagesByChatRoomIds(@Param("chatRoomIds") chatRoomIds: List<Long>): List<Message>


}