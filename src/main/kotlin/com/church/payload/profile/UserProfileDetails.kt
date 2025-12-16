package com.church.payload.profile

import com.church.model.account.RoleType
import com.church.model.account.UserRole
import java.util.UUID

class UserProfileDetails (
    val userId:UUID,
    val firstName: String,
    val lastName: String,
    val phone: String? = "",
    val address: String? ="",
    val role: List<RoleType>,
    val profilePictureUrl: String?="",
)

