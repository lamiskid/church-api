package com.church.payload.sermon

import com.church.model.profile.MediaType


data class SermonRequest(
    val title: String,
    val content: String,
    val preacher: String,
    val fileUrl: String,
    val mediaType: MediaType = MediaType.AUDIO
)
