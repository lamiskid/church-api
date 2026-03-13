package com.church.service

import com.church.model.donation.Donation
import com.church.model.donation.PaymentSession
import com.church.payload.donation.DonationRequest
import com.church.repository.DonationRepository
import com.church.security.User
import com.church.util.StripeServiceUtil
import com.stripe.model.PaymentIntent
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*

@Service
class DonationService (
    private val stripeService: StripeServiceUtil,
    private val donationRepository: DonationRepository
){

    @Transactional
    fun createDonation(user: User, request: DonationRequest): PaymentSession{

        val idempotencyKey = UUID.randomUUID().toString()

        val customer = stripeService.createCustomer(user.toAccount().email)

        val ephemeralKey = stripeService.createEphemeralKey(customer.id)


        val paymentIntent = stripeService.createPaymentIntent(
            request.amount,
            request.currency,
            customer.id,
            idempotencyKey
        )

        val donation = Donation(
            amount = request.amount,
            currency = request.currency,
            paymentIntentId = paymentIntent.id,
            status = paymentIntent.status,
            donorEmail = user.toAccount().email
        )

        donationRepository.save(donation)
        return PaymentSession(
            clientSecret = paymentIntent.clientSecret,
            customerId = customer.id,
            ephemeralKey = ephemeralKey.secret
        )
    }


    @Transactional
    fun updateDonationStatus(paymentIntent: PaymentIntent) {

        val donation = donationRepository
            .findByPaymentIntentId(paymentIntent.id)
            ?: return

        donation.status = paymentIntent.status
    }
}