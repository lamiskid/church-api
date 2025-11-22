package com.church.controller

import com.church.payload.profile.media.ConfirmUploadRequest
import com.church.payload.profile.media.ProfileMediaResponse
import com.church.payload.s3.PresignedUploadResponse
import com.church.security.User
import com.church.service.ProfileMediaService
import com.church.util.S3ServiceUtil
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/profile")
class ProfileController(
    private val s3ServiceUtil: S3ServiceUtil,
    private val profileMediaService: ProfileMediaService

) {

    @PostMapping("/picture")
    fun confirmProfilePicture(@AuthenticationPrincipal user: User,
                            @RequestBody confirmUploadRequest: ConfirmUploadRequest): ResponseEntity<ProfileMediaResponse> {
       return ResponseEntity.ok(profileMediaService.confirmUpload(user.toAccount(),confirmUploadRequest))
    }


    @PostMapping("/signed-url")
    fun generateUploadUrl(
        @RequestParam folder: String,
        @RequestParam fileName: String,
        @RequestParam contentType: String,
        @AuthenticationPrincipal user: User
    ): ResponseEntity<PresignedUploadResponse> {
        val result = s3ServiceUtil.generatePresignedUploadUrl(
            folder = folder,
            userId = user.getId(),
            fileName = fileName,
            contentType = contentType
        )
        return ResponseEntity.ok(result)
    }
}