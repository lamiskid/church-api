package com.church.payload.s3

import java.net.URL

data class PresignedUploadResponse(
    val uploadUrl: URL,
    val objectKey: String
)
