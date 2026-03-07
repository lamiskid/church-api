package com.church.model.donation

data class PaymentSession(
    val clientSecret: String,
    val customerId: String,
    val ephemeralKey: String
)