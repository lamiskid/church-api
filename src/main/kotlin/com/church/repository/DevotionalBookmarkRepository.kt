package com.church.repository


import com.church.model.devotional.DevotionalBookmark
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface DevotionalBookmarkRepository : JpaRepository<DevotionalBookmark, Long> {
    fun existsByUserIdAndDevotional_Id(userId: UUID, devotionalId: Long):Boolean
       @Query("""
       SELECT b.devotional.id
       FROM DevotionalBookmark b
       WHERE b.userId = :userId
         AND b.devotional.id IN :devotionalIds
   """)
       fun findBookmarkedIds(
           userId: UUID,
           devotionalIds: List<Long>
       ): List<Long>
  /*  @Query("""
    SELECT b.devotional.id
    FROM DevotionalBookmark b
    WHERE b.user.id = :userId
      AND b.devotional.id IN :devotionalIds
""")
    fun findBookmarkedIds(
        userId: UUID,
        devotionalIds: List<Long>
    ): List<Long>
*/


    fun findByUserIdAndDevotional_Id(userId: UUID, devotionalId: Long): DevotionalBookmark?
    fun findByUserIdAndDevotionalId(userId: UUID, devotionalId: Long): DevotionalBookmark?


}
