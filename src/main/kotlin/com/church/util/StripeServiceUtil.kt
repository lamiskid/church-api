package com.church.util

import com.stripe.model.Customer
import com.stripe.model.EphemeralKey
import com.stripe.model.PaymentIntent
import com.stripe.net.RequestOptions
import com.stripe.param.CustomerCreateParams
import com.stripe.param.EphemeralKeyCreateParams
import com.stripe.param.PaymentIntentCreateParams
import org.springframework.stereotype.Service

@Service
class StripeServiceUtil {

    fun createPaymentIntent1(
        amount: Long,
        currency: String,
        donorEmail: String?,
        idempotencyKey: String
    ): PaymentIntent {

        val params = PaymentIntentCreateParams.builder()
            .setAmount(amount)
            .setCurrency(currency)
            .putMetadata("donorEmail", donorEmail ?: "anonymous")
            .build()

        val options = RequestOptions.builder()
            .setIdempotencyKey(idempotencyKey)
            .build()

        return PaymentIntent.create(params, options)
    }

    fun createPaymentIntent(
        amount: Long,
        currency: String,
        customerId: String,
        idempotencyKey: String
    ): PaymentIntent {

        val params = PaymentIntentCreateParams.builder()
            .setAmount(amount)
            .setCurrency(currency)
            .setCustomer(customerId)
            .setAutomaticPaymentMethods(
                PaymentIntentCreateParams.AutomaticPaymentMethods
                    .builder()
                    .setEnabled(true)
                    .build()
            )
            .build()

        val options = RequestOptions.builder()
            .setIdempotencyKey(idempotencyKey)
            .build()

        return PaymentIntent.create(params, options)
    }



    fun createEphemeralKey(customerId: String): EphemeralKey {

        val params = EphemeralKeyCreateParams.builder()
            .setCustomer(customerId)
            .setStripeVersion("2023-10-16")
            .build()

        val options = RequestOptions.builder()
            .build()

        return EphemeralKey.create(params, options)
    }

    fun createCustomer(email: String): Customer {

        val params = CustomerCreateParams.builder()
            .setEmail(email)
            .build()

        return Customer.create(params)
    }
}