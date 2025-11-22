package com.church.payload.fcm

import com.church.model.fcm.DeviceType
import java.util.UUID

data class FcmTokenResponse(
    val id: Long,
    val token: String,
    val accountId: UUID,
    val deviceType: DeviceType,
    val createdAt: Long,
    val updatedAt: Long
)
