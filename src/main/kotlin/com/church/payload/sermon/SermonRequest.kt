package com.church.payload.sermon


data class SermonRequest(
    val title: String,
    val content: String,
    val preacher: String
)


