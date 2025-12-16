package com.church.controller

import com.church.payload.profile.ProfileRequest
import com.church.payload.profile.ProfileResponse
import com.church.payload.profile.media.ConfirmUploadRequest
import com.church.payload.profile.media.ProfileMediaResponse
import com.church.payload.s3.PresignedUploadResponse
import com.church.security.User
import com.church.service.ProfileMediaService
import com.church.service.ProfileService
import com.church.util.S3ServiceUtil
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/api/profile")
class ProfileController(
    private val s3ServiceUtil: S3ServiceUtil,
    private val profileService: ProfileService,
    private val profileMediaService:ProfileMediaService

) {

    @PostMapping("/picture")
    fun confirmProfilePicture(@AuthenticationPrincipal user: User,
                            @RequestBody confirmUploadRequest: ConfirmUploadRequest): ResponseEntity<ProfileMediaResponse> {
       return ResponseEntity.ok(profileMediaService.confirmUpload(user.toAccount(),confirmUploadRequest))
    }


    @PostMapping("/signed-url")
    fun generateUploadUrl(
        @RequestParam fileName: String,
        @RequestParam contentType: String,
        @AuthenticationPrincipal user: User
    ): ResponseEntity<PresignedUploadResponse> {
        val result = s3ServiceUtil.generatePresignedUploadUrl(
            folder = "picture",
            userId = user.getId(),
            fileName = fileName,
            contentType = contentType
        )
        return ResponseEntity.ok(result)
    }

    @GetMapping
    fun getProfile( @AuthenticationPrincipal user: User): ResponseEntity<ProfileResponse> {
        return ResponseEntity.ok(profileService.getProfile(user.getId()))
    }

    @PostMapping
    fun createProfile(
        @AuthenticationPrincipal user: User,
        @RequestBody request: ProfileRequest
    ): ResponseEntity<ProfileResponse> {
        return ResponseEntity.ok(profileService.createProfile(user.getId(), request))
    }

    @PutMapping
    fun updateProfile(
        @AuthenticationPrincipal user: User,
        @RequestBody request: ProfileRequest
    ): ResponseEntity<ProfileResponse> {
        return ResponseEntity.ok(profileService.updateProfile(user.getId(), request))
    }
}