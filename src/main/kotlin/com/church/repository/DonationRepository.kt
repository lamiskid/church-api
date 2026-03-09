package com.church.repository

import com.church.model.donation.Donation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DonationRepository : JpaRepository<Donation, UUID> {

    fun findByPaymentIntentId(paymentIntentId: String): Donation?
}