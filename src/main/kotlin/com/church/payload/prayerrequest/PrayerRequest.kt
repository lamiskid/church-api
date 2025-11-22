package com.church.payload.prayerrequest



data class PrayerRequestResponse(
    val id: Long?,
    val title: String,
    val message: String,
    val submittedBy: String,
    val isAnonymous: Boolean,
    val createdAt: Long
)

data class CreatePrayerRequest(
    val title: String,
    val message: String,
    val submittedBy: String,
    val isAnonymous: Boolean = false
)
