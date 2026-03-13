package com.church.payload.donation

data class DonationRequest(
    val amount: Long,
    val currency: String = "ngn",
)