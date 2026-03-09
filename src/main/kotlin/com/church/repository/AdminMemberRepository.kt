package com.church.repository

import com.church.model.account.Account
import com.church.payload.members.MemberAdminView
import com.church.payload.profile.MemberStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AdminMemberRepository : JpaRepository<Account, UUID> {


    @Query(
        """
        SELECT 
            a.id AS accountId,
            a.email AS email,
            p.firstName AS firstName,
            p.lastName AS lastName,
            p.phoneNumber AS phoneNumber,
            p.profilePictureUrl AS profilePictureUrl,
            a.createdAt AS createdAt
        FROM Account a
        JOIN a.profile p
        WHERE 
            (:profilePictureUrl IS NULL OR p.profilePictureUrl = :profilePictureUrl)
        AND (
            LOWER(p.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR
            LOWER(p.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR
            LOWER(a.email) LIKE LOWER(CONCAT('%', :search, '%')) OR
            p.phoneNumber LIKE CONCAT('%', :search, '%')
        )
        """, nativeQuery = true
    )
    fun findMembersForAdmin(
        @Param("search") search: String,
        @Param("profilePictureUrl") profilePictureUrl: String?,
        pageable: Pageable
    ): Page<MemberAdminView>


//    @Query("SELECT COUNT(p) FROM Profile p WHERE p.status = :status")
//    fun countByStatus(@Param("status") status: MemberStatus): Long

    @Query("SELECT COUNT(p) FROM Profile p")
    fun totalMembers(): Long
}