package com.church.service

import com.church.model.donation.Donation
import com.church.model.donation.PaymentSession
import com.church.payload.donation.DonationRequest
import com.church.repository.DonationRepository
import com.church.util.StripeServiceUtil
import com.stripe.model.PaymentIntent
import com.stripe.param.PaymentIntentCreateParams
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*

@Service
class DonationService (
    private val stripeService: StripeServiceUtil,
    private val donationRepository: DonationRepository
){



    @Transactional
    fun createDonation1(request: DonationRequest): Map<String, String> {

        val idempotencyKey = UUID.randomUUID().toString()

        val paymentIntent = stripeService.createPaymentIntent(
            request.amount,
            request.currency,
            request.donorEmail,
            idempotencyKey
        )

        val donation = Donation(
            amount = request.amount,
            currency = request.currency,
            paymentIntentId = paymentIntent.id,
            status = paymentIntent.status,
            donorEmail = request.donorEmail
        )

        donationRepository.save(donation)

        return mapOf(
            "clientSecret" to paymentIntent.clientSecret
        )
    }

    @Transactional
    fun createDonation(request: DonationRequest): PaymentSession{

        val idempotencyKey = UUID.randomUUID().toString()

        val customer = stripeService.createCustomer(request.donorEmail)

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
            donorEmail = request.donorEmail
        )

       // donationRepository.save(donation)
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