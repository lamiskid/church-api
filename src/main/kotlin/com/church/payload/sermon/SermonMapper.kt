package com.church.payload.sermon

import com.church.model.sermons.Sermon


fun Sermon.toResponseDto(): SermonResponse = SermonResponse(
    id = id,
    title = title,
    content = content,
    preacher = preacher,
    createdBy = createdBy,
    createdAt = createdAt,
    updatedAt = updatedAt
)
