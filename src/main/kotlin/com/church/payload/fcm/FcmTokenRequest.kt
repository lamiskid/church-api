package com.church.payload.fcm

import com.church.model.fcm.DeviceType
import java.util.UUID

data class FcmTokenRequest(
    val token: String,
    val deviceType: DeviceType
)
