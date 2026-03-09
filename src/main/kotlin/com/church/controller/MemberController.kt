package com.church.controller

import com.church.payload.members.MemberAdminView
import com.church.payload.profile.MemberStatsResponse
import com.church.payload.profile.MemberStatus
import com.church.service.MemberService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/admin/members")
class MemberController(
    private val adminMemberService: MemberService
) {

    @GetMapping
    fun getMembers(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "createdAt") sortBy: String,
        @RequestParam(defaultValue = "DESC") direction: Sort.Direction,
        @RequestParam(required = false) search: String?,
        @RequestParam(required = false) status: MemberStatus?
    ): Page<MemberAdminView> {
        return adminMemberService.getMembers(
            page, size, sortBy, direction, search, status
        )
    }

//    @GetMapping("/stats")
//    fun getStats(): MemberStatsResponse {
//        return adminMemberService.getMemberStats()
//    }
}
