package com.church.payload.profile.media

import com.church.model.profile.MediaType

data class ConfirmUploadRequest(
    val signedUploadUrl: String,
    val mediaUrl: String
)

data class ProfileMediaResponse(
    val type: MediaType = MediaType.IMAGE,
    val mediaUrl: String?,
)
