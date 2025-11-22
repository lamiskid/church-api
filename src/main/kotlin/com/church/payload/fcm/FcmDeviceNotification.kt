package com.church.payload.fcm

data class FcmDeviceNotification(
    val eventType: NotificationType,
    val routeId: String
)