package com.church.payload.members

import com.church.payload.profile.MemberStatus
import java.util.*

interface MemberAdminView {
    val accountId: UUID
    val email: String
    val firstName: String
    val lastName: String
    val phoneNumber: String?
    val status: MemberStatus
    val createdAt: Long
}
