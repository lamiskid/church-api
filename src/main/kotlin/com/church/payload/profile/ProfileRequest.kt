package com.church.payload.profile

data class ProfileRequest(
    val firstName: String,
    val lastName: String,
    val phone: String?,
    val address: String?,
    val profilePictureUrl: String?
)
