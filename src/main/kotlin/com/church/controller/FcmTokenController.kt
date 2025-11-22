package com.church.controller

import com.church.payload.fcm.FcmTokenRequest
import com.church.payload.fcm.FcmTokenResponse
import com.church.security.User
import com.church.service.FcmTokenService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/fcm")
class FcmTokenController(
    private val fcmTokenService: FcmTokenService
) {

    @PostMapping("/register")
    fun registerToken( @AuthenticationPrincipal user: User,@RequestBody request: FcmTokenRequest): FcmTokenResponse {
        return fcmTokenService.registerOrUpdateToken(user,request)
    }

    @GetMapping("/account/{accountId}")
    fun getTokenByAccount(@PathVariable accountId: UUID): List<FcmTokenResponse> {
        return fcmTokenService.getTokensByAccountId(accountId)
    }
}
