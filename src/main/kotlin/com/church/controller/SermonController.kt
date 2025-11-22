package com.church.controller

import com.church.payload.pagination.PageResponse
import com.church.payload.profile.media.ConfirmUploadRequest
import com.church.payload.s3.PresignedUploadResponse
import com.church.payload.sermon.SermonRequest
import com.church.payload.sermon.SermonResponse
import com.church.security.User
import com.church.service.SermonService
import com.church.util.S3ServiceUtil
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.UUID


@RestController
@RequestMapping("/api/sermons")
class SermonController(
    private val sermonService: SermonService,
    private val s3ServiceUtil: S3ServiceUtil
) {

    @PreAuthorize("hasAnyRole('PASTOR','ADMIN')")
    @PostMapping
    fun createSermon(@AuthenticationPrincipal user: User, @RequestBody sermonRequest: SermonRequest): ResponseEntity<PresignedUploadResponse> {
        val response = sermonService.createSermon(sermonRequest, user.getId())
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PostMapping("/confirm-upload/{sermonId}")
    fun confirmSermonUpload(@AuthenticationPrincipal user: User,@PathVariable sermonId: UUID, @RequestBody confirmUploadRequest: ConfirmUploadRequest): ResponseEntity<SermonResponse> {
        val response = sermonService.confirmSermonUpload(user,sermonId,confirmUploadRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PreAuthorize("hasAnyRole('PASTOR','ADMIN')")
    @PutMapping("/{id}")
    fun updateSermon(@AuthenticationPrincipal user: User,@PathVariable id: UUID, @RequestBody sermonRequest: SermonRequest): ResponseEntity<SermonResponse> {
        val response = sermonService.updateSermon(user.getId(),id, sermonRequest,)
        return ResponseEntity.ok(response)
    }


    @GetMapping
    fun getAllSermons(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<PageResponse<SermonResponse>> {
        return ResponseEntity.ok(sermonService.getAllSermons(page, size))
    }

    @GetMapping("/{id}")
    fun getSermonById(@AuthenticationPrincipal user: User,@PathVariable id: UUID): ResponseEntity<SermonResponse> {
        return ResponseEntity.ok(sermonService.getSermonById(id))
    }

    @PreAuthorize("hasAnyRole('PASTOR','ADMIN')")
    @DeleteMapping("/{id}")
    fun deleteSermon(@AuthenticationPrincipal user: User,@PathVariable id: UUID): ResponseEntity<Unit> {
        sermonService.deleteSermon(id, user.getId())
        return ResponseEntity.noContent().build()
    }

}
