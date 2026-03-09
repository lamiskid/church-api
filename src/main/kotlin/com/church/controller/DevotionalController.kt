package com.church.controller


import com.church.payload.devotional.DevotionalBookmarkResponse
import com.church.payload.devotional.DevotionalRequest
import com.church.payload.devotional.DevotionalResponse
import com.church.payload.pagination.PageResponse
import com.church.security.User
import com.church.service.DevotionalService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/devotionals")
class DevotionalController(
    private val devotionalService: DevotionalService
) {

    @PostMapping
    fun createDevotional(
        @RequestBody request: DevotionalRequest
    ): DevotionalResponse {
        return devotionalService.create(request)
    }

    @GetMapping
    fun getDevotionals(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @AuthenticationPrincipal user: User
    ): PageResponse<DevotionalResponse> {
        return devotionalService.getAll(page = page, size = size, userId = user.getId())
    }

    @GetMapping("/{id}")
    fun getDevotional(
        @PathVariable id: Long,
        @AuthenticationPrincipal user: User
    ): DevotionalResponse {
        return devotionalService.getOne(id, user.getId())
    }

    @PostMapping("/{id}/bookmark")
    fun bookmark(
        @PathVariable id: Long,
        @AuthenticationPrincipal user: User
    ): ResponseEntity<DevotionalBookmarkResponse> {
        val bookmarked = devotionalService.toggleBookmark(userId = user.getId(), devotionalId = id)

        return ResponseEntity.ok(
            DevotionalBookmarkResponse(
                devotionalId = id,
                bookmarked = bookmarked))
    }
}

