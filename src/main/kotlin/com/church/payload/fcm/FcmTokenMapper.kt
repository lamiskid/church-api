package com.church.payload.fcm

import com.church.model.fcm.FcmToken

object FcmTokenMapper {

fun toResponse(fcmToken: FcmToken): FcmTokenResponse = FcmTokenResponse(
    id = fcmToken.id,
    token = fcmToken.token,
    accountId = fcmToken.account.id!!,
    deviceType = fcmToken.deviceType,
    createdAt = fcmToken.createdAt,
    updatedAt = fcmToken.updatedAt
)
}
