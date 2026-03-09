package com.church.service

import com.church.payload.members.MemberAdminView
import com.church.payload.profile.MemberStatsResponse
import com.church.payload.profile.MemberStatus
import com.church.repository.AdminMemberRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val adminMemberRepository: AdminMemberRepository
) {

    fun getMembers(
        page: Int,
        size: Int,
        sortBy: String,
        direction: Sort.Direction,
        search: String?,
        status: MemberStatus?
    ): Page<MemberAdminView> {

        val pageable = PageRequest.of(
            page,
            size,
           Sort.by(direction, sortBy)
        )

        return adminMemberRepository.findMembersForAdmin(
            search = search ?: "",
            profilePictureUrl = "",
            pageable = pageable
        )
    }

    /*fun getMemberStats(): MemberStatsResponse {
        return MemberStatsResponse(
            total = adminMemberRepository.totalMembers(),
            active = adminMemberRepository.countByStatus(MemberStatus.ACTIVE),
            inactive = adminMemberRepository.countByStatus(MemberStatus.INACTIVE),
            suspended = adminMemberRepository.countByStatus(MemberStatus.SUSPENDED)
        )
    }*/
}


