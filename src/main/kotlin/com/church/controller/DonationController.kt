package com.church.controller

import com.church.model.donation.PaymentSession
import com.church.payload.donation.DonationRequest
import com.church.service.DonationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/donations")
class DonationController(
    private val donationService: DonationService
) {

    @PostMapping
    fun createDonation(
        @RequestBody request: DonationRequest
    ): ResponseEntity<PaymentSession> {
        require(request.amount > 0) { "Amount must be greater than 0" }
        val response = donationService.createDonation(request)

        return ResponseEntity.ok(response)
    }
}