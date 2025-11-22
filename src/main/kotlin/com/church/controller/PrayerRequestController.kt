package com.church.controller

import com.church.payload.pagination.PageResponse
import com.church.payload.prayerrequest.CreatePrayerRequest
import com.church.payload.prayerrequest.PrayerRequestResponse
import com.church.security.User
import com.church.service.CentrifugoService
import com.church.service.PrayerRequestService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID


@RestController
@RequestMapping("/api/prayers")
class PrayerRequestController(
    private val prayerRequestService: PrayerRequestService,
    private val centrifugoService: CentrifugoService
) {

    @PostMapping
    fun createPrayerRequest(
        @RequestBody createPrayerRequest: CreatePrayerRequest,
        @AuthenticationPrincipal user: User
    ): ResponseEntity<PrayerRequestResponse> {
        val newRequest = prayerRequestService.createPrayerRequest(
            user,createPrayerRequest)

        // Push real-time update to Centrifugo “prayers” channel
       // centrifugoService.publishPrayerCreated(newRequest)

        return ResponseEntity.ok(newRequest)
    }

    @GetMapping("/approved")
    fun getApprovedPrayers(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<PageResponse<PrayerRequestResponse>> {
        val response = prayerRequestService.getApprovedPrayerRequests(page, size)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('PASTOR','ADMIN')")
    fun getPendingPrayers(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<PageResponse<PrayerRequestResponse>> {
        val response = prayerRequestService.getPendingPrayerRequests(page, size)
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('PASTOR','ADMIN')")
    fun approvePrayer(
        @PathVariable id: UUID,
        @AuthenticationPrincipal user: User
    ): ResponseEntity<PrayerRequestResponse> {
        val approvedPrayer = prayerRequestService.approvePrayerRequest(id)

        // Broadcast approved prayer to wall (real-time)
       // centrifugoService.publishPrayerApproved(approvedPrayer, approvedBy = user.username)

        return ResponseEntity.ok(approvedPrayer)
    }
}