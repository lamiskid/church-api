package com.church.repository

import com.church.model.chat.ChatRoom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ChatRoomRepository : JpaRepository<ChatRoom, Long> {

/*    fun findByParticipants_Id(userId: Long): List<ChatRoom>

    @Query("""
        SELECT cr FROM ChatRoom cr 
        JOIN cr.participants u1 
        JOIN cr.participants u2 
        WHERE cr.type = 'PRIVATE' 
        AND u1.id = :user1Id 
        AND u2.id = :user2Id 
        GROUP BY cr.id 
        HAVING COUNT(cr.participants) = 2
    """)
    fun findPrivateChatRoomBetween(user1Id: Long, user2Id: Long): ChatRoom?*/

    fun findAllByParticipantsId(userId: UUID?): List<ChatRoom>
}

